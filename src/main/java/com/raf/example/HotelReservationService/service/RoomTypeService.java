package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.RoomType;
import com.raf.example.HotelReservationService.dto.RoomTypeDto;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
import com.raf.example.HotelReservationService.mapper.Mapper;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import com.raf.example.HotelReservationService.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoomTypeService {
    private RoomTypeRepository roomTypeRepository;
    private HotelRepository hotelRepository;

    private Mapper mapper;

    public RoomTypeService(RoomTypeRepository roomTypeRepository, HotelRepository hotelRepository, Mapper mapper) {
        this.roomTypeRepository = roomTypeRepository;
        this.hotelRepository = hotelRepository;
        this.mapper = mapper;
    }

    public RoomTypeDto save(Long managerId , RoomTypeDto roomTypeDto){
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findByHotelIdAndTypeName(hotel.getId(), roomTypeDto.getTypeName());
        if(roomTypeOptional.isPresent())
            throw new OperationNotAllowed(String.format("Room type '%s' for a hotel '%s' already exist.", roomTypeDto.getTypeName(), hotel.getName()));

        roomTypeDto.setHotelId(hotel.getId());
        roomTypeRepository.save(mapper.dtoToRoomType(roomTypeDto));
        return roomTypeDto;
    }

    public RoomTypeDto remove(Long id){
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(id);
        if(roomTypeOptional.isPresent()){
            RoomType roomType = roomTypeOptional.get();
            roomTypeRepository.deleteById(roomType.getId());
            return mapper.roomTypeToDto(roomType);
        }
        return null;
    }

    public RoomTypeDto update(Long managerId, RoomTypeDto roomTypeDto){
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findByHotelIdAndTypeName(hotel.getId(), roomTypeDto.getTypeName());
        if(roomTypeOptional.isPresent()){
            RoomType roomType = roomTypeOptional.get();
            roomTypeDto.setHotelId(hotel.getId());
            roomTypeRepository.save(mapper.updateRoomType(roomType, roomTypeDto));
            return roomTypeDto;
        }
        else
            throw new OperationNotAllowed(String.format("Room type '%s' for a hotel '%s' does not exist.", roomTypeDto.getTypeName(), hotel.getName()));
    }
}
