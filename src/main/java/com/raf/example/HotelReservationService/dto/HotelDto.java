package com.raf.example.HotelReservationService.dto;

import javax.validation.constraints.NotNull;

public class HotelDto {

    private String city;
    private String name;
    private String description;
    private Integer numberOfRooms = 0;
    private Long managerId;

    public HotelDto() {
    }

    public HotelDto(String city, String name, String description, Integer numberOfRooms, Long managerId) {
        this.city = city;
        this.name = name;
        this.description = description;
        this.numberOfRooms = numberOfRooms;
        this.managerId = managerId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
