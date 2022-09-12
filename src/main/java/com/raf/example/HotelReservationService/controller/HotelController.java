package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.dto.HotelDto;
import com.raf.example.HotelReservationService.dto.PriceChangeDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
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

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/changeHotelName")
    @ApiOperation(value = "change hotel name")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<HotelDto> changeHotelName(@RequestHeader("authorization") String authorization, String newHotelName) {
        return new ResponseEntity<>(hotelService.changeHotelName(authorization.split(" ")[1], newHotelName), HttpStatus.CREATED);
    }

    @PostMapping("/changeNumberOrRooms")
    @ApiOperation(value = "change number of hotel rooms")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<HotelDto> changeNumOfHotelRooms(@RequestHeader("authorization") String authorization, Integer newNumOfRooms) {
        return new ResponseEntity<>(hotelService.changeNumOfHotelRooms(authorization.split(" ")[1], newNumOfRooms), HttpStatus.CREATED);
    }

    @PostMapping("/chagePriceForRoomType")
    @ApiOperation(value = "change price for room type")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<PriceChangeDto> changePriceForRoomType(@RequestHeader("authorization") String authorization, PriceChangeDto priceChangeDto) {
        return new ResponseEntity<>(hotelService.changePriceForRoomType(authorization.split(" ")[1], priceChangeDto), HttpStatus.CREATED);
    }

    @PostMapping("/changeDescription")
    @ApiOperation(value = "change description")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<HotelDto> changeHotelDescription(@RequestHeader("authorization") String authorization, String newDescription) {
        return new ResponseEntity<>(hotelService.changeHotelDescription(authorization.split(" ")[1], newDescription), HttpStatus.CREATED);
    }
}
