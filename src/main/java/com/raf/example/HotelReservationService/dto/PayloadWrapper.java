package com.raf.example.HotelReservationService.dto;

public class PayloadWrapper {
    private Long id;
    private String role;

    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
