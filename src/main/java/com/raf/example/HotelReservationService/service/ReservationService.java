package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.clientUserService.dto.DiscountDto;
import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.dto.*;
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

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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

    public ReservationService(RoomRepository roomRepository, HotelRepository hotelRepository, ReservationRepository reservationRepository,
                              JmsTemplate jmsTemplate, MessageHelper messageHelper, RestTemplate userServiceRestTemplate) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    public List<RoomDto> listAvailableRooms(AvailableRoomsFilterDto availableRoomsFilterDto){
        List<Room> rooms = roomRepository.findAll();

        if(rooms == null)
            return null;
        
        rooms = rooms.stream()
                .filter(r -> {
                    Optional<Reservation> reservationOptional = reservationRepository.findByRoomId(r.getId());
                    if(reservationOptional.isPresent()) {
                        Reservation reservation = reservationOptional.get();
                        Hotel hotel = hotelRepository.findById(reservation.getHotelId()).get();
                        if(availableRoomsFilterDto.getCity() != null && !availableRoomsFilterDto.getCity().equalsIgnoreCase(hotel.getCity()))
                            return false;

                        if(availableRoomsFilterDto.getHotelName() != null && availableRoomsFilterDto.getHotelName().equalsIgnoreCase(hotel.getName()))
                            return false;

                        if(availableRoomsFilterDto.getStartDate() != null && availableRoomsFilterDto.getEndDate() != null &&
                                availableRoomsFilterDto.getStartDate().isBefore(availableRoomsFilterDto.getEndDate())) {

                            if(!availableRoomsFilterDto.getEndDate().isBefore(reservation.getStartDate()) || !availableRoomsFilterDto.getStartDate().isAfter(reservation.getEndDate()))
                                return false;
                        }else {
                            LocalDate now = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                            return now.isBefore(reservation.getStartDate()) || now.isAfter(reservation.getEndDate());
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());


        if(availableRoomsFilterDto.getSort() != null){
            if(availableRoomsFilterDto.getSort().equalsIgnoreCase("ASC"))
                Collections.sort(rooms, (o1, o2) -> Double.compare(o1.getPricePerDay(),o2.getPricePerDay()));
            else if(availableRoomsFilterDto.getSort().equalsIgnoreCase("DESC"))
                Collections.sort(rooms, (o1, o2) -> Double.compare(o2.getPricePerDay(),o1.getPricePerDay()));
        }
        
        List<RoomDto> roomsDto = new ArrayList<>();
        for(Room room : rooms){
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomNumber(room.getRoomNumber());
            roomDto.setType(room.getType());
            roomDto.setPricePerDay(room.getPricePerDay());
            roomDto.setHotelId(roomDto.getHotelId());

            roomsDto.add(roomDto);
        }

        return roomsDto;
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

        Double totalPrice = room.getPricePerDay() *
                (Period.between(reservationDto.getStartDate(), reservationDto.getEndDate()).getDays() + 1);
        totalPrice-= totalPrice*(discountDtoResponseEntity.getBody().getDiscount()/100);

        reservationRepository.save(new Reservation(reservationDto.getClientId(), reservationDto.getClientEmail(), room.getHotelId(), room.getId(),
                reservationDto.getStartDate(), reservationDto.getEndDate(), totalPrice, true));

        jmsTemplate. convertAndSend("increment_reservation_queue",
                messageHelper.createTextMessage(new IncrementReservationDto(reservationDto.getClientId(), true)));
        return reservationDto;
    }

    public Reservation removeReservation(Long reservationId){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();
            reservationRepository.deleteById(reservation.getId());
            jmsTemplate. convertAndSend("increment_reservation_queue",
                    messageHelper.createTextMessage(new IncrementReservationDto(reservation.getUserId(), false)));
            return reservation;
        }

        return null;
    }

}
