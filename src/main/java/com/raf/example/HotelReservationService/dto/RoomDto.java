package com.raf.example.HotelReservationService.dto;

public class RoomDto {
    private Long id;
    private Long hotelId;
    private Integer roomNumber;
    private String typeName;
    private Double pricePerDay;

    public RoomDto() {
    }

    public RoomDto(Integer roomNumber, String typeName) {
        this.roomNumber = roomNumber;
        this.typeName = typeName;
    }

    public RoomDto(Long id, Long hotelId, Integer roomNumber, String typeName) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.typeName = typeName;
    }

    public RoomDto(Long hotelId, Integer roomNumber, String typeName, Double pricePerDay) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.typeName = typeName;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
