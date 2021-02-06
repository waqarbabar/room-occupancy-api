package com.task.tech.services;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;

import java.util.List;

public interface BookingService {

    BookingEstimation getBookingEstimates(RoomAvailability roomAvailabilityDTO);

    void saveGuestRates(List<Integer> newRates);
}
