package com.raf.example.HotelReservationService.repository;

import com.raf.example.HotelReservationService.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    Optional<RoomType> findByName(String name);
}
