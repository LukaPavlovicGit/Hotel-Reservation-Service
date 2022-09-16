package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Review;
import com.raf.example.HotelReservationService.dto.ReviewDto;
import com.raf.example.HotelReservationService.exception.NotFoundException;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ReservationRepository reservationRepository;

    public ReviewService(ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReviewDto addReview(Long clientId, ReviewDto reviewDto){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reviewDto.getReservationId());
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();
            if(reservation.getUserId() != clientId)
                throw new OperationNotAllowed(String.format("Client is not allowed to comment reservation with ID %s.", reservation.getId()));

            reviewRepository.save(new Review(clientId, reservation.getId(), reviewDto.getRate(), reviewDto.getComment()));
        }

        throw new NotFoundException(String.format("Reservation with ID %s not found.", reviewDto.getReservationId()));
    }
}
