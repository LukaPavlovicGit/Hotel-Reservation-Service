package com.raf.example.HotelReservationService.dto;

public class RoomTypeDto {
    private String typeName;
    private Double pricePerDay;

    public RoomTypeDto() {
    }

    public RoomTypeDto(String typeName, Double pricePerDay) {
        this.typeName = typeName;
        this.pricePerDay = pricePerDay;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
