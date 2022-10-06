package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.domain.Room;
import com.raf.example.HotelReservationService.dto.AvailableRoomsFilterDto;
import com.raf.example.HotelReservationService.dto.ReviewDto;
import com.raf.example.HotelReservationService.dto.RoomDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.RoomService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
                                           @RequestBody RoomDto roomDto) {
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(roomService.save(managerId, roomDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<RoomDto> removeRoom(@RequestHeader("Authorization") String authorization,
                                              @RequestParam Long roomId){
        Long managerId = securityAspect.getUserId(authorization);
        return new ResponseEntity<>(roomService.remove(managerId, roomId), HttpStatus.OK);
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_CLIENT", "ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<Page<RoomDto>> getAvailableRooms(@RequestHeader("Authorization") String authorization,
                                                           @RequestBody AvailableRoomsFilterDto availableRoomsFilterDto,
                                                           @ApiIgnore Pageable pageable){
        return new ResponseEntity<>(roomService.listAvailableRooms(availableRoomsFilterDto,pageable), HttpStatus.OK);
    }

}
