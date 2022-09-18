package com.raf.example.HotelReservationService.dto;

public class RoomDto {
    private Long hotelId;
    private Integer roomNumber;
    private String type;
    private Double pricePerDay;

    public RoomDto() {
    }

    public RoomDto(Long hotelId, Integer roomNumber, String type, Double pricePerDay) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerDay = pricePerDay;
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
