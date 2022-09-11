package com.raf.example.HotelReservationService.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = {@Index(columnList = "name", unique = true)})
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer numberOfRooms;
    @OneToMany(mappedBy="Hotel")
    private List<Reservation> reservations = new ArrayList<>();


    public Hotel() {
    }

    public Long getId() {
        return id;
    }
}
