package com.raf.example.HotelReservationService.dto;

public class IncrementReservationDto {
    private Long userId;
    private boolean increment;

    public IncrementReservationDto(){}

    public IncrementReservationDto(Long userId, boolean increment) {
        this.userId = userId;
        this.increment = increment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
