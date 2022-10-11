package com.raf.example.HotelReservationService.repository;

import com.raf.example.HotelReservationService.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReservationIdAndUserId(Long reservationId, Long userId);
}
