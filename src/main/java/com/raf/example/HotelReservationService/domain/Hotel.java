package com.raf.example.HotelReservationService.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {@Index(columnList = "name", unique = true)})
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long managerId;

    @NotNull
    private String city;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Integer numberOfRooms;

    public Hotel(Long managerId, String city, String name, String description, Integer numberOfRooms) {
        this.managerId = managerId;
        this.city = city;
        this.name = name;
        this.description = description;
        this.numberOfRooms = numberOfRooms;
    }

    public Hotel() {}

    public Long getId() {
        return id;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
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
}
