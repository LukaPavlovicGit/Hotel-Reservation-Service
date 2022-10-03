package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.dto.ReservationDto;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


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
    public ResponseEntity<ReservationDto> addNewReservation(@RequestHeader("Authorization") String authorization,
                                                            @RequestParam(required = false, value = "roomId") Long roomId,
                                                            @RequestParam(required = false, value = "startDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
                                                            @RequestParam(required = false, value = "endDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate) {
        Long clientId = securityAspect.getUserId(authorization);
        String clientEmail = securityAspect.getUserEmail(authorization);
        return new ResponseEntity<>(reservationService.save(new ReservationDto(roomId,clientId,clientEmail,startDate,endDate)), HttpStatus.CREATED);
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_MANAGER"})
    public ResponseEntity<List<ReservationDto>> getAllReservations(@RequestHeader("Authorization") String authorization){

        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @CheckSecurity(roles = {"ROLE_CLIENT", "ROLE_MANAGER"})
    public ResponseEntity<ReservationDto> removeReservation(@RequestHeader("Authorization") String authorization,
                                                            @PathVariable Long reservationId) {

        Long clientId = securityAspect.getUserId(authorization);
        String userRole = securityAspect.getUserRole(authorization);
        return new ResponseEntity<>(reservationService.removeReservation(reservationId, clientId, userRole), HttpStatus.OK);
    }
}
