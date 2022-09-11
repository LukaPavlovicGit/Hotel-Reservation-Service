package com.raf.example.HotelReservationService.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = {@Index(columnList = "name", unique = true)})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long hotelId;

    @NotNull
    private Integer roomNumber;

    @NotNull
    private String type;

    @NotNull
    private Double pricePerDay;

    public Room(Long hotelId, Integer roomNumber, String type, Double pricePerDay) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerDay = pricePerDay;
    }

    public Room() {}

    public Long getId() {
        return id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

}
