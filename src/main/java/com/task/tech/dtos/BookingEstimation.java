package com.task.tech.dtos;

import lombok.Data;

@Data
public class BookingEstimation {
    private RoomBooking premiumBooking;
    private RoomBooking economyBooking;
}
