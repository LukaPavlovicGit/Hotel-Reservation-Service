package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.domain.RoomType;
import com.raf.example.HotelReservationService.dto.AvailableRoomsFilterDto;
import com.raf.example.HotelReservationService.dto.RoomDto;
import com.raf.example.HotelReservationService.exception.NotFoundException;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.repository.RoomRepository;
import com.raf.example.HotelReservationService.repository.RoomTypeRepository;
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
    private RoomTypeRepository roomTypeRepository;
    private HotelRepository hotelRepository;
    private ReservationRepository reservationRepository;
    private HotelService hotelService;

    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository, HotelRepository hotelRepository,
                       ReservationRepository reservationRepository, HotelService hotelService) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.hotelService = hotelService;
    }

    public RoomDto save(Long managerId, String roomTypeName, Integer roomNumber){

        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        Long hotelId = hotel.getId();
        RoomType roomType = roomTypeRepository.findByHotelNameAndTypeName(hotel.getName(),roomTypeName).orElseThrow(() -> new NotFoundException(String.format("Room type with a name %s not found.", roomTypeName)));

        Room room = new Room(hotelId, roomNumber, roomType);
        roomRepository.save(room);
        hotelService.incrementNumberOfRooms(hotelId,true);

        return new RoomDto(hotelId, roomNumber, roomType.getTypeName(), roomType.getPricePerDay());
    }

    public RoomDto remove(Long managerId, Long roomId){
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        Long hotelId = hotel.getId();
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(String.format("Room with id %s not found", roomId)));
        roomRepository.deleteById(roomId);
        hotelService.incrementNumberOfRooms(hotelId, false);
        return new RoomDto(room.getHotelId(), room.getRoomNumber(), room.getRoomType().getTypeName(), room.getRoomType().getPricePerDay());
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
                        }
                        else {
                            LocalDate now = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                            if(now.isAfter(reservation.getStartDate()) && now.isBefore(reservation.getEndDate()))
                                return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if(availableRoomsFilterDto.getSort() != null){
            if(availableRoomsFilterDto.getSort().equalsIgnoreCase("ASC"))
                Collections.sort(rooms, (o1, o2) -> Double.compare(o1.getRoomType().getPricePerDay(),o2.getRoomType().getPricePerDay()));
            else if(availableRoomsFilterDto.getSort().equalsIgnoreCase("DESC"))
                Collections.sort(rooms, (o1, o2) -> Double.compare(o2.getRoomType().getPricePerDay(),o1.getRoomType().getPricePerDay()));
        }

        List<RoomDto> roomsDto = new ArrayList<>();
        for(Room room : rooms){
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomNumber(room.getRoomNumber());
            roomDto.setType(room.getRoomType().getTypeName());
            roomDto.setPricePerDay(room.getRoomType().getPricePerDay());
            roomDto.setHotelId(roomDto.getHotelId());

            roomsDto.add(roomDto);
        }

        return roomsDto;
    }

}
