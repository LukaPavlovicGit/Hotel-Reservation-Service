package com.raf.example.HotelReservationService.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ReviewDto {
    private Long reservationId;
    private Long clientId;
    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;
    @NotNull
    private String comment;

    public ReviewDto(){}

    public ReviewDto(Long reservationId, Long clientId, Integer rate, String comment) {
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.rating = rate;
        this.comment = comment;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
