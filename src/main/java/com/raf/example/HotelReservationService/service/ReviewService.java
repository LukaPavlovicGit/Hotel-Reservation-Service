package com.raf.example.HotelReservationService.service;

import com.raf.example.HotelReservationService.domain.Hotel;
import com.raf.example.HotelReservationService.domain.Reservation;
import com.raf.example.HotelReservationService.domain.Review;
import com.raf.example.HotelReservationService.dto.ReviewDto;
import com.raf.example.HotelReservationService.exception.NotFoundException;
import com.raf.example.HotelReservationService.exception.OperationNotAllowed;
import com.raf.example.HotelReservationService.repository.HotelRepository;
import com.raf.example.HotelReservationService.repository.ReservationRepository;
import com.raf.example.HotelReservationService.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ReservationRepository reservationRepository;
    private HotelRepository hotelRepository;

    public ReviewService(ReviewRepository reviewRepository, ReservationRepository reservationRepository, HotelRepository hotelRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
    }

    public ReviewDto save(Long clientId ,ReviewDto reviewDto){

        Optional<Reservation> reservationOptional = reservationRepository.findById(reviewDto.getReservationId());
        if(reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();
            if(reservation.getUserId() != clientId)
                throw new OperationNotAllowed(String.format("Client is not allowed to comment reservation with ID %s.", reservation.getId()));

            reviewRepository.save(new Review(clientId, reservation.getId(), reviewDto.getRating(), reviewDto.getComment()));
        }

        throw new NotFoundException(String.format("Reservation with ID %s not found.", reviewDto.getReservationId()));
    }

    public List<ReviewDto> getReviews(String hotelName, String city){

        List<Review> reviews = reviewRepository.findAll();
        reviews = reviews.stream().filter(r -> {
            Reservation reservation = reservationRepository.findById(r.getReservationId()).orElseThrow(() -> new NotFoundException("exception occurred in ReviewService class : reservation not found"));
            Hotel hotel = hotelRepository.findById(reservation.getHotelId()).orElseThrow(() -> new NotFoundException("exception occurred in ReviewService class : hotel not found"));
            if ((hotelName == null || hotelName.equals(hotel.getName())) && (city == null || city.equals(hotel.getCity())))
                return true;
            else
                return false;
        })
        .collect(Collectors.toList());

        List<ReviewDto> ans = new ArrayList<>();
        for(Review r : reviews)
            ans.add(new ReviewDto(r.getReservationId(), r.getRating(), r.getComment()));

        return (ans.isEmpty() ? null : ans);
    }

    public ReviewDto remove(Long reviewId, String userRole, Long clientId){

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException(String.format("Review with id %s not found.", reviewId)));

        if(userRole.equals("ROLE_CLIENT")){
            if(review.getUserId() != clientId)
                throw new OperationNotAllowed(String.format("Client not allowed to delete review with id %s.", clientId));
        }
        ReviewDto reviewDto = new ReviewDto(review.getReservationId(), review.getRating(), review.getComment());
        reviewRepository.deleteById(reviewId);
        return reviewDto;
    }

    public ReviewDto update(Long id, Long clientId, ReviewDto reviewDto){
        Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Review with id %s not found.", id)));
        if(review.getUserId() != clientId)
            throw new OperationNotAllowed(String.format("Client not allowed to delete review with id %s.", clientId));

        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        reviewRepository.save(review);

        return reviewDto;
    }

    public List<Hotel> getTopRatedHotels() {
        List<Hotel> topHotels = hotelRepository.findAll();
        HashMap<Hotel, Integer> cnt = new HashMap<>();
        HashMap<Hotel, Double> rating = new HashMap<>();

        for (Review review: reviewRepository.findAll()) {
            Reservation reservation = reservationRepository.findById(review.getReservationId()).get();
            Hotel hotel = hotelRepository.findById(reservation.getHotelId()).get();

            Integer val = 0;
            Double sum = 0.0;
            if (cnt.containsKey(hotel)) val = cnt.get(hotel);
            if (rating.containsKey(hotel)) sum = rating.get(hotel);

            cnt.put(hotel, val + 1);
            rating.put(hotel, sum + review.getRating());

            if (!topHotels.contains(hotel))
                topHotels.add(hotel);
        }

        topHotels.sort(Comparator.comparingDouble(o -> (rating.get(o) / cnt.get(o))));
        Collections.reverse(topHotels);

        return topHotels.subList(0, Math.min(3, topHotels.size()));
    }

}
