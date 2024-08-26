package com.finalproject.stayease.bookings.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookingItemReqDto {
    private Long roomId;
    private Date checkInDate;
    private Date checkOutDate;
    private Double price;
    private int totalAdults;
    private int totalChildren;
    private int totalInfants;
}
