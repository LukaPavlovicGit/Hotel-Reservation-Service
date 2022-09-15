package com.raf.example.HotelReservationService.dto;

public class MessageDto {
    private String type;
    private String name;
    private String lastname;
    private String hotelName;
    private String email;
    private String rezStart;

    public MessageDto(String type, String name, String lastname, String email, String hotelName, String rezStart) {
        this.type = type;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.hotelName = hotelName;
        this.rezStart = rezStart;
    }

    public MessageDto() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRezStart() {
        return rezStart;
    }

    public void setRezStart(String rezStart) {
        this.rezStart = rezStart;
    }
}
