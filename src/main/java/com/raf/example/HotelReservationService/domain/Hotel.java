package com.raf.example.HotelReservationService.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table (indexes = {@Index(columnList = "name", unique = true)})
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private Long managerId;

    @NotNull
    private String city;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Integer numberOfRooms = 0;


    public Hotel() {}

    public Hotel(Long id, Long managerId, String city, String name, String description, Integer numberOfRooms) {
        this.id = id;
        this.managerId = managerId;
        this.city = city;
        this.name = name;
        this.description = description;
        this.numberOfRooms = numberOfRooms;
    }

    public Hotel(Long managerId, String city, String name, String description, Integer numberOfRooms) {
        this.managerId = managerId;
        this.city = city;
        this.name = name;
        this.description = description;
        this.numberOfRooms = numberOfRooms;
    }

    public Hotel(Long managerId, String city, String name, String description) {
        this.managerId = managerId;
        this.city = city;
        this.name = name;
        this.description = description;
    }

    public Hotel(String city, String name, String description){
        this.managerId = managerId;
        this.city = city;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
