package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.dto.HotelDto;
import com.raf.example.HotelReservationService.dto.PriceChangeDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.HotelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private HotelService hotelService;
    private SecurityAspect securityAspect;

    public HotelController(HotelService hotelService, SecurityAspect securityAspect) {
        this.hotelService = hotelService;
        this.securityAspect = securityAspect;
    }

    @PostMapping("/addHotel")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<HotelDto> addHotel(@RequestHeader("authorization") String authorization,
                                                      @RequestBody @Valid HotelDto hotelDto) {
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(hotelService.addHotel(managerId,hotelDto), HttpStatus.CREATED);
    }

    @PutMapping("/updateManager")
    @ApiOperation(value = "manager update")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<HotelDto> updateManager(@RequestHeader("authorization") String authorization,
                                                    @RequestBody HotelDto hotelDto){
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(hotelService.update(managerId, hotelDto), HttpStatus.OK);
    }

}