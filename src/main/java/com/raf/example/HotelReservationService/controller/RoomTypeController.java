package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.RoomTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roomTypes")
public class RoomTypeController {
    private SecurityAspect securityAspect;
    private RoomTypeService roomTypeService;

    public RoomTypeController(SecurityAspect securityAspect, RoomTypeService roomTypeService) {
        this.securityAspect = securityAspect;
        this.roomTypeService = roomTypeService;
    }


}
