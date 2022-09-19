package com.raf.example.HotelReservationService.domain;

import javax.persistence.*;

@Entity
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String typeName;
    private Double pricePerDay;
    private Long hotelId;

    /*
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;*/


    public RoomType() { }

    public RoomType(String typeName, Double pricePerDay, Long hotelId) {
        this.typeName = typeName;
        this.pricePerDay = pricePerDay;
        this.hotelId = hotelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String name) {
        this.typeName = name;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
