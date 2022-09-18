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

    public HotelDto save(Long managerId, HotelDto hotelDto){
        Optional<Hotel> hotelOptional = hotelRepository.findHotelByManagerId(managerId);
        if(!hotelOptional.isPresent()) {
            Hotel hotel = hotelOptional.get();
            hotel.setName(hotelDto.getName());
            hotel.setManagerId(managerId);
            hotel.setCity(hotelDto.getCity());
            hotel.setDescription(hotelDto.getDescription());
            hotel.setNumberOfRooms(hotelDto.getNumberOfRooms());
            return hotelDto;
        }

        throw new OperationNotAllowed("Manager with a ID: " + managerId + " is already a manager!");
    }

    public HotelDto update(Long managerId, HotelDto hotelDto){

        Hotel hotel = hotelRepository.findHotelByManagerId(managerId)
                .orElseThrow(() -> new NotFoundException("Hotel with a name " + hotelDto.getName().toUpperCase() + " not found"));

        hotel.setCity(hotelDto.getCity());
        hotel.setName(hotelDto.getName());
        hotel.setDescription(hotelDto.getDescription());
        hotel.setNumberOfRooms(hotelDto.getNumberOfRooms());
        hotelRepository.save(hotel);
        return hotelDto;

    }

    public HotelDto remove(Long managerId){
        Hotel hotel = hotelRepository.findHotelByManagerId(managerId).get();
        hotelRepository.deleteById(hotel.getId());
        return new HotelDto(hotel.getCity(), hotel.getName(), hotel.getDescription(), hotel.getNumberOfRooms());
    }

    public void incrementNumberOfRooms(Long id, boolean increment){
        hotelRepository.findById(id).ifPresent(hotel -> {
            if(increment)
                hotel.setNumberOfRooms(hotel.getNumberOfRooms() + 1);
            else
                hotel.setNumberOfRooms(hotel.getNumberOfRooms() - 1);
            hotelRepository.save(hotel);
        });
    }

}
