package com.raf.example.HotelReservationService.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long reservationId;
    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;
    @NotNull
    private String comment;

    public Review() {}

    public Review(Long id, Long userId, Long reservationId, Integer rating, String comment) {
        this.id = id;
        this.userId = userId;
        this.reservationId = reservationId;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(Long userId, Long reservationId, Integer rating, String comment) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
