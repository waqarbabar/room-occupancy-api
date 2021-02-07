package com.task.tech.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookingEstimation {

    private RoomTierBooking premiumTier;
    private RoomTierBooking economyTier;
}
