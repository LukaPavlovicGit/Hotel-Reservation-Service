package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.dto.HotelDto;
import com.raf.example.HotelReservationService.exception.NotFoundException;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
import com.raf.example.HotelReservationService.mapper.Mapper;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class HotelService {

    private HotelRepository hotelRepository;
    private Mapper mapper;

    public HotelService(HotelRepository hotelRepository, Mapper mapper) {
        this.hotelRepository = hotelRepository;
        this.mapper = mapper;
    }

    public HotelDto save(HotelDto hotelDto){
        Optional<Hotel> hotelOptional = hotelRepository.findHotelByManagerId(hotelDto.getManagerId());
        if(hotelOptional.isPresent())
            throw new OperationNotAllowed("Manager with a ID: " + hotelDto.getManagerId() + " is already a manager!");
        hotelRepository.save(mapper.dtoToHotel(hotelDto));
        return hotelDto;
    }

    public HotelDto update(HotelDto hotelDto){
        Hotel hotel = hotelRepository.findHotelByManagerId(hotelDto.getManagerId())
                .orElseThrow(() -> new NotFoundException("Hotel with a name " + hotelDto.getName().toUpperCase() + " not found"));
        hotelRepository.save(mapper.updateHotel(hotel, hotelDto));
        return hotelDto;

    }

    public HotelDto remove(Long managerId){
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        hotelRepository.deleteById(hotel.getId());
        return mapper.hotelToDto(hotel);
    }

    public void incrementNumberOfRooms(Long id){
        hotelRepository.findById(id).ifPresent(hotel -> {
            hotel.setNumberOfRooms(hotel.getNumberOfRooms() + 1);
            hotelRepository.save(hotel);
        });
    }

    public void decrementNumberOfRooms(Long id){
        hotelRepository.findById(id).ifPresent(hotel -> {
            hotel.setNumberOfRooms(hotel.getNumberOfRooms() - 1);
            hotelRepository.save(hotel);
        });
    }

}
