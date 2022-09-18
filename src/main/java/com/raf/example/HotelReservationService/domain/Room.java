package com.raf.example.HotelReservationService.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table (indexes = {@Index(columnList = "roomNumber", unique = true)})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long hotelId;
    @NotNull
    private Integer roomNumber;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @NotNull
    private RoomType roomType;

    public Room(Long hotelId, Integer roomNumber, RoomType roomType) {
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    public Room() {}

    public Long getId() {
        return id;
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

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
