package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.dto.ReservationDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private SecurityAspect securityAspect;

    private ReservationService reservationService;

    public ReservationController(SecurityAspect securityAspect,
                                 ReservationService reservationService) {
        this.securityAspect = securityAspect;
        this.reservationService = reservationService;
    }

    @PostMapping
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReservationDto> addNewReservation(@RequestHeader("authorization") String authorization,
                                                            @RequestBody ReservationDto reservationDto) {
        Long clientId = securityAspect.getUserId(authorization);
        String clientEmail = securityAspect.getUserEmail(authorization);
        reservationDto.setClientId(clientId);
        reservationDto.setClientEmail(clientEmail);
        return new ResponseEntity<>(reservationService.save(reservationDto), HttpStatus.CREATED);
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<Page<ReservationDto>> getAllReservations(@RequestHeader("authorization") String authorization,
                                                                   @ApiIgnore Pageable pageable){
        return new ResponseEntity<>(reservationService.getAllReservations(pageable), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @CheckSecurity(roles = {"ROLE_CLIENT", "ROLE_MANAGER"})
    public ResponseEntity<ReservationDto> removeReservation(@RequestHeader("authorization") String authorization,
                                                            @PathVariable("id") Long id) {

        Long clientId = securityAspect.getUserId(authorization);
        String userRole = securityAspect.getUserRole(authorization);
        return new ResponseEntity<>(reservationService.removeReservation(id, clientId, userRole), HttpStatus.OK);
    }
}
