package com.raf.example.HotelReservationService.clientUserService.dto;

public class DiscountDto {
    private Integer discount;

    public DiscountDto(Integer discount) {
        this.discount = discount;
    }

    public DiscountDto() {}

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}