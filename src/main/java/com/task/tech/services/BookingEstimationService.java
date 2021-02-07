package com.task.tech.services;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;

import java.util.List;

public interface BookingEstimationService {

    BookingEstimation getBookingEstimation(RoomAvailability roomAvailability, List<Integer> premiumRates, List<Integer> economyRates);
}
