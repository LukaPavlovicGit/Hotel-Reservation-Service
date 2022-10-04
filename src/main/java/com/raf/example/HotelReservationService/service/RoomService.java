package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.domain.RoomType;
import com.raf.example.HotelReservationService.dto.AvailableRoomsFilterDto;
import com.raf.example.HotelReservationService.dto.RoomDto;
import com.raf.example.HotelReservationService.exception.NotFoundException;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
import com.raf.example.HotelReservationService.mapper.Mapper;
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
    private Mapper mapper;

    public RoomService(RoomRepository roomRepository, RoomTypeRepository roomTypeRepository, HotelRepository hotelRepository,
                       ReservationRepository reservationRepository, HotelService hotelService, Mapper mapper) {
        this.roomRepository = roomRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.hotelService = hotelService;
        this.mapper = mapper;
    }

    public RoomDto save(Long managerId, RoomDto roomDto){
        System.out.println("Room service : save");
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        Long hotelId = hotel.getId();
        RoomType roomType = roomTypeRepository.findByHotelIdAndTypeName(hotel.getId(),roomDto.getTypeName())
                .orElseThrow(() -> new NotFoundException(String.format("Room type with a name %s not found.", roomDto.getTypeName())));

        Optional<Room> roomOptional = roomRepository.findByRoomNumber(roomDto.getRoomNumber());
        if(roomOptional.isPresent())
            throw new OperationNotAllowed(String.format("Room with a room number %s already exist. Choose different room number.", roomDto.getRoomNumber()));

        Room room = new Room(hotelId, roomDto.getRoomNumber(), roomType);
        roomRepository.save(room);
        hotelService.incrementNumberOfRooms(hotelId);
        System.out.println("Room service : save 2");
        return mapper.roomToDto(room);
    }

    public RoomDto remove(Long managerId, Long roomId){
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        Long hotelId = hotel.getId();
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException(String.format("Room with id %s not found", roomId)));
        roomRepository.deleteById(roomId);
        hotelService.decrementNumberOfRooms(hotelId);
        return mapper.roomToDto(room);
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
            roomsDto.add(mapper.roomToDto(room));
        }

        return roomsDto;
    }
}
