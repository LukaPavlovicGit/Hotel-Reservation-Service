package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.dto.ReservationDto;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.ReservationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;


@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private SecurityAspect securityAspect;
    private ReservationRepository reservationRepository;

    private ReservationService reservationService;

    public ReservationController(SecurityAspect securityAspect, ReservationRepository reservationRepository, ReservationService reservationService) {
        this.securityAspect = securityAspect;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    @PostMapping
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReservationDto> addNewReservation(@RequestHeader("Authorization") String authorization,
                                                            @RequestParam Long roomId,
                                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate startDate,
                                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate) {
        Long clientId = securityAspect.getUserId(authorization);
        String clientEmail = securityAspect.getUserEmail(authorization);
        return new ResponseEntity<>(reservationService.addReservation(new ReservationDto(roomId,clientId,clientEmail,startDate,endDate)), HttpStatus.CREATED);
    }
}