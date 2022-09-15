package com.raf.example.HotelReservationService.repository;

import com.raf.example.HotelReservationService.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByRoomId(Long aLong);
    List<Reservation> findAllByRoomId(Long roomId);
}
