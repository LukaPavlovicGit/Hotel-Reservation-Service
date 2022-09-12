package com.raf.example.HotelReservationService.repository;

import com.raf.example.HotelReservationService.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
