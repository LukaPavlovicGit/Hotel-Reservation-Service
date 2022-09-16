package com.raf.example.HotelReservationService.dto;

public class ReviewDto {
    private Long reservationId;
    private Integer rate;
    private String comment;

    public ReviewDto(){}

    public ReviewDto(Long reservationId, Integer rate, String comment) {
        this.reservationId = reservationId;
        this.rate = rate;
        this.comment = comment;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
