package com.raf.example.HotelReservationService.dto;

public class IncrementReservationDto {
    private Long userId;

    public IncrementReservationDto(){}

    public IncrementReservationDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
