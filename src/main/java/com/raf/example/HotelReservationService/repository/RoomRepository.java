package com.raf.example.HotelReservationService.repository;

import com.raf.example.HotelReservationService.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
