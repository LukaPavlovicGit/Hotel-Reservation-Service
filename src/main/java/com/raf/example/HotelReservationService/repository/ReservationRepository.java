package com.raf.example.HotelReservationService.repository;

import com.raf.example.HotelReservationService.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
