package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.clientUserService.dto.DiscountDto;
import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.dto.*;
import com.raf.example.HotelReservationService.exception.NotFoundException;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
import com.raf.example.HotelReservationService.messageHelper.MessageHelper;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.repository.RoomRepository;
import io.github.resilience4j.retry.Retry;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Period;
import java.util.*;

@Service
@Transactional
public class ReservationService {
    private RoomRepository roomRepository;
    private HotelRepository hotelRepository;
    private ReservationRepository reservationRepository;
    private JmsTemplate jmsTemplate;
    private MessageHelper messageHelper;
    private RestTemplate userServiceRestTemplate;
    private Retry userServiceRetry;

    public ReservationService(RoomRepository roomRepository, HotelRepository hotelRepository, ReservationRepository reservationRepository,
                              JmsTemplate jmsTemplate, MessageHelper messageHelper, RestTemplate userServiceRestTemplate, Retry userServiceRetry) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.userServiceRetry = userServiceRetry;
    }

    public ReservationDto addReservation(ReservationDto reservationDto){

        List<Reservation> reservations = reservationRepository.findAllByRoomId(reservationDto.getRoomId());
        for(Reservation r : reservations){
            if(reservationDto.getEndDate().isBefore(r.getStartDate()) || reservationDto.getStartDate().isAfter(r.getEndDate()))
                continue;
            throw new OperationNotAllowed("Room not available in chosen period.");
        }
        Room room = roomRepository.findById(reservationDto.getRoomId()).get();
        //get discount from user service
        ResponseEntity<DiscountDto> discountDtoResponseEntity =  Retry.decorateSupplier(userServiceRetry, () -> userServiceRestTemplate.exchange("/user/" +
                reservationDto.getClientId() + "/discount", HttpMethod.GET, null, DiscountDto.class)).get();

        Double totalPrice = room.getRoomType().getPricePerDay() *
                (Period.between(reservationDto.getStartDate(), reservationDto.getEndDate()).getDays() + 1);
        totalPrice-= totalPrice*(discountDtoResponseEntity.getBody().getDiscount()/100);

        reservationRepository.save(new Reservation(reservationDto.getClientId(), reservationDto.getClientEmail(), room.getHotelId(), room.getId(),
                reservationDto.getStartDate(), reservationDto.getEndDate(), totalPrice, true));

        jmsTemplate. convertAndSend("increment_reservation_queue",
                messageHelper.createTextMessage(new IncrementReservationDto(reservationDto.getClientId(), true)));
        return reservationDto;
    }

    public Reservation removeReservation(Long reservationId, Long userId, String role){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();

            if(role.equals("ROLE_CLIENT")) {
                if (reservation.getUserId() != userId)
                    throw new OperationNotAllowed(String.format("Client not allowed to delete reservation with id %s.", reservationId));
            }

            reservationRepository.deleteById(reservation.getId());
            jmsTemplate. convertAndSend("increment_reservation_queue",
                    messageHelper.createTextMessage(new IncrementReservationDto(reservation.getUserId(), false)));
            return reservation;
        }

        throw new NotFoundException(String.format("Reservation with id %s not found.", reservationId));
    }
}
