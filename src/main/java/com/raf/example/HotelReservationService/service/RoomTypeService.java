package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.RoomType;
import com.raf.example.HotelReservationService.dto.RoomTypeDto;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
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

    public RoomTypeDto save(Long managerId ,RoomTypeDto roomTypeDto){
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findByHotelNameAndTypeName(hotel.getName(), roomTypeDto.getTypeName());
        if(roomTypeOptional.isPresent())
            throw new OperationNotAllowed(String.format("Room type '%s' for a hotel '%s' already exist.", roomTypeDto.getTypeName(), hotel.getName()));

        RoomType roomType = new RoomType(roomTypeDto.getTypeName(), roomTypeDto.getPricePerDay(), hotel.getName());
        roomTypeRepository.save(roomType);

        return new RoomTypeDto(roomType.getTypeName(), roomType.getPricePerDay());
    }

    public RoomTypeDto remove(Long id){
        Optional<RoomType> roomTypeOptional = roomTypeRepository.findById(id);
        if(roomTypeOptional.isPresent()){
            RoomType roomType = roomTypeOptional.get();
            roomTypeRepository.deleteById(roomType.getId());
            return new RoomTypeDto(roomType.getTypeName(), roomType.getPricePerDay());
        }
        return null;
    }

    private RoomTypeDto update(Long id){

        return null;
    }
}
