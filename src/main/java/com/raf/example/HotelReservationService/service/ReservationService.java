package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.dto.FiltersDto;
import com.raf.example.HotelReservationService.dto.IncrementReservationDto;
import com.raf.example.HotelReservationService.dto.RoomDto;
import com.raf.example.HotelReservationService.messageHelper.MessageHelper;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.repository.RoomRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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

    public ReservationService(RoomRepository roomRepository, HotelRepository hotelRepository, ReservationRepository reservationRepository,
                              JmsTemplate jmsTemplate, MessageHelper messageHelper, RestTemplate userServiceRestTemplate) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.jmsTemplate = jmsTemplate;
        this.messageHelper = messageHelper;
        this.userServiceRestTemplate = userServiceRestTemplate;
    }

    public List<RoomDto> listAvailableRooms(FiltersDto filtersDto){
        List<Room> rooms = roomRepository.findAll();

        if(rooms == null)
            return null;
        
        rooms = rooms.stream()
                .filter(r -> {
                    Optional<Reservation> reservationOptional = reservationRepository.findByRoomId(r.getId());
                    if(reservationOptional.isPresent()) {
                        Reservation reservation = reservationOptional.get();
                        LocalDate now = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                        return now.isBefore(reservation.getStartDate()) || now.isAfter(reservation.getEndDate());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if(filtersDto.getCity() != null){
            rooms = rooms.stream()
                    .filter(r -> {
                        Optional<Hotel> hotelOptional = hotelRepository.findById(r.getHotelId());
                        if(hotelOptional.isPresent()){
                            return filtersDto.getCity().equalsIgnoreCase(hotelOptional.get().getCity());
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }
        if(filtersDto.getHotelName() != null){
            rooms = rooms.stream()
                    .filter(r -> {
                        Optional<Hotel> hotelOptional = hotelRepository.findById(r.getHotelId());
                        if(hotelOptional.isPresent()){
                            return filtersDto.getHotelName().equalsIgnoreCase(hotelOptional.get().getName());
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }
        if(filtersDto.getEndDate() != null && filtersDto.getEndDate() != null && filtersDto.getStartDate().isBefore(filtersDto.getEndDate())){
            rooms = rooms.stream()
                    .filter(r -> {
                        Optional<Reservation> reservationOptional = reservationRepository.findByRoomId(r.getId());
                        if(reservationOptional.isPresent()){
                            Reservation reservation = reservationOptional.get();
                            return reservation.getStartDate().isAfter(filtersDto.getEndDate()) || reservation.getEndDate().isBefore(filtersDto.getStartDate());
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        if(filtersDto.getSort() != null){
            if(filtersDto.getSort().equalsIgnoreCase("ASC")){
                Collections.sort(rooms, new Comparator<Room>() {
                    @Override
                    public int compare(Room o1, Room o2) {
                        return o1.getPricePerDay() - o2.getPricePerDay();
                    }
                });
            }
            else if(filtersDto.getSort().equalsIgnoreCase("DESC")){
                Collections.sort(rooms, new Comparator<Room>() {
                    @Override
                    public int compare(Room o1, Room o2) {
                        return o2.getPricePerDay() - o1.getPricePerDay();
                    }
                });
            }
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

    public Reservation addReservation(Reservation reservation){
        IncrementReservationDto incrementReservationDto = new IncrementReservationDto();
        incrementReservationDto.setUserId(3L);
        jmsTemplate. convertAndSend("increment_queue", messageHelper.createTextMessage(new IncrementReservationDto()));
        return null;
    }

}
