package com.raf.example.HotelReservationService.service;

import ch.qos.logback.core.net.server.Client;
import com.raf.example.HotelReservationService.clientUserService.dto.DiscountDto;
import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.dto.*;
import com.raf.example.HotelReservationService.exception.NotFoundException;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
import com.raf.example.HotelReservationService.mapper.Mapper;
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

import java.awt.image.ImageProducer;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

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
    private Mapper mapper;

    public ReservationService(RoomRepository roomRepository, HotelRepository hotelRepository, ReservationRepository reservationRepository, JmsTemplate jmsTemplate,
                              MessageHelper messageHelper, RestTemplate userServiceRestTemplate, Retry userServiceRetry, Mapper mapper) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.userServiceRetry = userServiceRetry;
        this.mapper = mapper;
    }

    public ReservationDto save(ReservationDto reservationDto){

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

        reservationDto.setPrice(totalPrice);
        reservationDto.setHotelId(room.getHotelId());
        Reservation reservation = mapper.dtoToReservation(reservationDto);
        reservationRepository.save(reservation);

        userServiceRestTemplate.exchange("http://localhost:8080/api/users/incrementReservation/"+reservationDto.getClientId(),
                HttpMethod.POST, null, ResponseEntity.class);

        sendEmail(reservation, "reservation_successful");
        return reservationDto;
    }

    public List getAllReservations(){
        return reservationRepository.findAll().stream().map(mapper::reservationToDto).collect(Collectors.toList());
    }

    public ReservationDto removeReservation(Long reservationId, Long userId, String role){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();

            if(role.equals("ROLE_CLIENT")) {
                if (reservation.getUserId() != userId)
                    throw new OperationNotAllowed(String.format("Client not allowed to delete reservation with id %s.", reservationId));
            }
            reservationRepository.deleteById(reservation.getId());
            userServiceRestTemplate.exchange("http://localhost:8080/api/users/decrementReservation/"+reservation.getUserId(),
                    HttpMethod.POST, null, ResponseEntity.class);

            sendEmail(reservation, "reservation_cancellation");
            return mapper.reservationToDto(reservation);
        }
        throw new NotFoundException(String.format("Reservation with id %s not found.", reservationId));
    }

    public void sendEmail(Reservation reservation, String emailType) {
        Hotel hotel = hotelRepository.findById(reservation.getHotelId())
                .orElseThrow(() -> new NotFoundException(String.format("Hotel with id %s not found.", reservation.getHotelId())));
        ResponseEntity<UserDto> userDto = userServiceRestTemplate.exchange("http://localhost:8080/api/users/get/" +
                reservation.getUserId(), HttpMethod.GET, null, UserDto.class);

        MessageDto messageDto = new MessageDto(userDto.getBody().getFirstName(), userDto.getBody().getLastName(), userDto.getBody().getEmail(), hotel.getName());
        jmsTemplate.convertAndSend("send_mail_queue", messageHelper.createTextMessage(messageDto));
    }
}
