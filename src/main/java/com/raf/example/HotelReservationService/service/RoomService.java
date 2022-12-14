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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<RoomDto> listAvailableRooms(AvailableRoomsFilterDto availableRoomsFilterDto, Pageable pageable){
        List<Room> rooms = roomRepository.findAll();

        if(rooms == null)
            return null;

        rooms = rooms.stream()
                .filter(r -> {
                    Hotel hotel = hotelRepository.findById(r.getHotelId()).get();
                    if(availableRoomsFilterDto.getHotelName() != null && !availableRoomsFilterDto.getHotelName().equalsIgnoreCase(hotel.getName()) ||
                            availableRoomsFilterDto.getCity() != null && !availableRoomsFilterDto.getCity().equalsIgnoreCase(hotel.getCity()))
                        return false;

                    LocalDate start = availableRoomsFilterDto.getStartDate();
                    LocalDate end = availableRoomsFilterDto.getEndDate();

                    if(start != null && end != null && start.isBefore(end)) {
                        List<Reservation> reservationList = reservationRepository.findAllByRoomId(r.getId());

                        for (Reservation reservation : reservationList) {
                            LocalDate reservationStart = reservation.getStartDate();
                            LocalDate reservationEnd = reservation.getEndDate();
                            if ( (start.equals(reservationStart) || start.isAfter(reservationStart)) && (start.equals(reservationEnd) || start.isBefore(reservationEnd)) ||
                                    (end.isEqual(reservationEnd) || end.isBefore(reservationEnd)) && (end.isEqual(reservationStart) || end.isAfter(reservationStart)))
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
                Collections.sort(rooms, (o2, o1) -> Double.compare(o2.getRoomType().getPricePerDay(),o1.getRoomType().getPricePerDay()));
        }

        List<RoomDto> roomsDto = new ArrayList<>();
        for(Room room : rooms){
            roomsDto.add(mapper.roomToDto(room));
        }

        return new PageImpl<>(roomsDto);
    }
    public RoomDto update(Long managerId, RoomDto roomDto){
        roomRepository.findById(roomDto.getId()).orElseThrow(() -> new NotFoundException("Room not found"));
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        RoomType roomType = roomTypeRepository.findByHotelIdAndTypeName(hotel.getId(),roomDto.getTypeName())
                .orElseThrow(() -> new NotFoundException(String.format("Room type with a name %s not found.", roomDto.getTypeName())));

        Room update = mapper.dtoToRoom(roomDto);
        update.setRoomType(roomType);
        roomRepository.save(update);

        return roomDto;
    }
}
