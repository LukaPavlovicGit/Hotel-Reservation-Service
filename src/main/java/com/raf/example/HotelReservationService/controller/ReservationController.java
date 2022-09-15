package com.raf.example.HotelReservationService.controller;

import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.dto.ReservationDto;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.secutiry.CheckSecurity;
import com.raf.example.HotelReservationService.secutiry.SecurityAspect;
import com.raf.example.HotelReservationService.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Reservation> addNewReservation(@RequestHeader("Authorization") String authorization, @RequestBody ReservationDto reservationDto) {
        Long clientId = securityAspect.getUserAttribute(authorization, "USER_ID");
        String clientEmail = securityAspect.getUserAttribute(authorization, "USER_EMAIL");
        System.out.println(clientEmail + " " + clientId);
        return new ResponseEntity<>(reservationService.addReservation(clientId, reservationDto), HttpStatus.CREATED);
    }
}
