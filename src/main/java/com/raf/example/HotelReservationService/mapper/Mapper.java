package com.raf.example.HotelReservationService.mapper;

import com.raf.example.HotelReservationService.domain.*;
import com.raf.example.HotelReservationService.dto.*;
import com.raf.example.HotelReservationService.service.ReviewService;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public RoomDto roomToDto(Room room){
        return new RoomDto(room.getHotelId(), room.getRoomNumber(), room.getRoomType().getTypeName(), room.getRoomType().getPricePerDay());
    }
    public Hotel dtoToHotel(HotelDto hotelDto){
        return new Hotel(hotelDto.getManagerId(), hotelDto.getCity(), hotelDto.getName(), hotelDto.getDescription());
    }
    public HotelDto hotelToDto(Hotel hotel){
        return new HotelDto(hotel.getCity(), hotel.getName(), hotel.getDescription(), hotel.getNumberOfRooms(), hotel.getManagerId());
    }
    public Hotel updateHotel(Hotel hotel, HotelDto hotelDto){
        return new Hotel(hotel.getId(), hotelDto.getManagerId(), hotelDto.getCity(), hotelDto.getName(), hotelDto.getDescription(), hotelDto.getNumberOfRooms());
    }
    public Reservation dtoToReservation(ReservationDto reservationDto){
        return new Reservation(reservationDto.getClientId(), reservationDto.getClientEmail(), reservationDto.getHotelId(), reservationDto.getRoomId(),
                reservationDto.getStartDate(), reservationDto.getEndDate(), reservationDto.getPrice(), true);
    }
    public ReservationDto reservationToDto(Reservation reservation){
        return new ReservationDto(reservation.getRoomId(), reservation.getUserId(), reservation.getUserEmail(), reservation.getStartDate(), reservation.getEndDate(), reservation.getHotelId());
    }
    public Review dtoToReview(ReviewDto reviewDto){
        return new Review(reviewDto.getClientId(), reviewDto.getReservationId(), reviewDto.getRating(), reviewDto.getComment());
    }
    public ReviewDto reviewToDto(Review review){
        return new ReviewDto(review.getReservationId(), review.getUserId(), review.getRating(), review.getComment());
    }
    public Review updateReview(Review review, ReviewDto reviewDto){
        return new Review(review.getId(), review.getUserId(), review.getReservationId(), reviewDto.getRating(), reviewDto.getComment());
    }
    public RoomType dtoToRoomType(RoomTypeDto roomTypeDto){
        return new RoomType(roomTypeDto.getTypeName(), roomTypeDto.getPricePerDay(), roomTypeDto.getHotelId());
    }
    public RoomTypeDto roomTypeToDto(RoomType roomType) {
        return new RoomTypeDto(roomType.getTypeName(), roomType.getPricePerDay(), roomType.getHotelId());
    }
    public RoomType updateRoomType(RoomType roomType, RoomTypeDto roomTypeDto){
        return new RoomType(roomTypeDto.getTypeName(), roomTypeDto.getPricePerDay(), roomType.getHotelId());
    }
}
