package com.raf.example.HotelReservationService.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservationDto {

    private Long roomId;
    private Long clientId;

    private String clientEmail;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public ReservationDto(){

    }

    public ReservationDto(Long roomId, Long clientId, String clientEmail, LocalDate startDate, LocalDate endDate) {
        this.roomId = roomId;
        this.clientId = clientId;
        this.clientEmail = clientEmail;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
