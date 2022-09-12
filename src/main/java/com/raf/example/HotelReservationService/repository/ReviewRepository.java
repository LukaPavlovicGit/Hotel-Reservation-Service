package com.raf.example.HotelReservationService.repository;

import com.raf.example.HotelReservationService.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
