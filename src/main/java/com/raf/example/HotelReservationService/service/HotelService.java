package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.dto.HotelDto;
import com.raf.example.HotelReservationService.dto.PriceChangeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HotelService {


    public HotelDto changeHotelName(String token, String newHotelName){
        return null;
    }
    public HotelDto changeNumOfHotelRooms(String token, Integer num){
        return null;
    }

    public PriceChangeDto changePriceForRoomType(String token, PriceChangeDto priceChangeDto){
        return null;
    }

    public HotelDto changeHotelDescription(String token, String newDescription){
        return null;
    }
}
