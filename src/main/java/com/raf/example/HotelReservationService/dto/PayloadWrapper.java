package com.raf.example.HotelReservationService.dto;

public class PayloadWrapper {
    private Long id;
    private String role;

    public PayloadWrapper(){ }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
