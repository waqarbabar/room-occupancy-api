package com.task.tech.services.impl;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.services.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {


    @Override
    public void saveGuestRates(List<Integer> newRates) {

    }

    @Override
    public BookingEstimation getBookingEstimates(RoomAvailability roomAvailability) {
        return null;
    }
}
