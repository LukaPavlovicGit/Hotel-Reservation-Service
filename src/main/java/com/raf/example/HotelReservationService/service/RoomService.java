package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.dto.AvailableRoomsFilterDto;
import com.raf.example.HotelReservationService.dto.HotelDto;
import com.raf.example.HotelReservationService.dto.RoomDto;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService {

    private RoomRepository roomRepository;
    private HotelRepository hotelRepository;
    private ReservationRepository reservationRepository;

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

}
