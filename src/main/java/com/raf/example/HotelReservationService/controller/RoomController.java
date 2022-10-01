package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.dto.AvailableRoomsFilterDto;
import com.raf.example.HotelReservationService.dto.RoomDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.RoomService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private SecurityAspect securityAspect;
    private RoomService roomService;

    public RoomController(SecurityAspect securityAspect, RoomService roomService) {
        this.securityAspect = securityAspect;
        this.roomService = roomService;
    }

    @PostMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<RoomDto> addRoom(@RequestHeader("Authorization") String authorization,
                                           @RequestParam(required = false, value = "roomType") String roomType,
                                           @RequestParam(required = false, value = "roomNumber") Integer roomNumber){
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(roomService.save(managerId, roomType, roomNumber), HttpStatus.CREATED);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<RoomDto> removeRoom(@RequestHeader("Authorization") String authorization,
                                              @RequestParam Long roomId){
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(roomService.remove(managerId, roomId), HttpStatus.OK);
    }

    @GetMapping
    public List<RoomDto> getAvailableRooms(@RequestBody AvailableRoomsFilterDto availableRoomsFilterDto){
        return roomService.listAvailableRooms(availableRoomsFilterDto);
    }

}
