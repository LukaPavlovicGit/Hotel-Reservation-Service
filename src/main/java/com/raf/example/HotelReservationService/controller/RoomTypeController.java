package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.dto.RoomTypeDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.RoomTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomTypes")
public class RoomTypeController {
    private SecurityAspect securityAspect;
    private RoomTypeService roomTypeService;

    public RoomTypeController(SecurityAspect securityAspect, RoomTypeService roomTypeService) {
        this.securityAspect = securityAspect;
        this.roomTypeService = roomTypeService;
    }

    @PostMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<RoomTypeDto> addRoomType(@RequestHeader("Authorization") String authorization,
                                                   @RequestBody RoomTypeDto roomTypeDto){
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(roomTypeService.save(managerId, roomTypeDto), HttpStatus.CREATED);
    }

    @PostMapping("/all")
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<List<RoomTypeDto>> addAllRoomTypes(@RequestHeader("Authorization") String authorization,
                                                             @RequestBody List<RoomTypeDto> roomTypeDtos){
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(roomTypeService.saveAll(managerId, roomTypeDtos), HttpStatus.CREATED);
    }

    @PutMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<RoomTypeDto> updateRoomType(@RequestHeader("Authorization") String authorization,
                                                      @RequestBody RoomTypeDto roomTypeDto){
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(roomTypeService.update(managerId, roomTypeDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<RoomTypeDto> removeRoomType(@RequestHeader("Authorization") String authorization,
                                                      @RequestBody Long id){
        return new ResponseEntity<>(roomTypeService.remove(id), HttpStatus.CREATED);
    }

}
