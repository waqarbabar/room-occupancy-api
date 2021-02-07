package com.task.tech.services.impl;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.exceptions.EntityNotFoundException;
import com.task.tech.services.BookingEstimationService;
import com.task.tech.services.BookingService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.partitioningBy;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingEstimationService bookingEstimationService;

    private final List<Integer> guestRates;
    @Setter
    @Value("${booking.premium-threshold}")
    private Integer premiumMinimumThreshold;

    public BookingServiceImpl(BookingEstimationService bookingEstimationService) {
        this.bookingEstimationService = bookingEstimationService;
        this.guestRates = new ArrayList<>();
    }

    @Override
    public void saveGuestRates(List<Integer> newRates) {
        guestRates.clear();
        guestRates.addAll(newRates);
    }

    @Override
    public BookingEstimation getBookingEstimates(RoomAvailability roomAvailability) {
        this.validate();
        Map<Boolean, List<Integer>> sortedRatesMap = getSortedRates();
        List<Integer> premiumRates = sortedRatesMap.get(true);
        log.debug("total premium rates={}", premiumRates.size());
        List<Integer> economyRates = sortedRatesMap.get(false);
        log.debug("total economy rates={}", economyRates.size());
        return bookingEstimationService.getBookingEstimation(roomAvailability, premiumRates, economyRates);
    }

    private Map<Boolean, List<Integer>> getSortedRates() {
        return guestRates
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(partitioningBy(offeredRate -> offeredRate >= premiumMinimumThreshold));
    }

    private void validate() {
        if (isEmpty(guestRates)) {
            throw new EntityNotFoundException("no estimates found because customer rates does not exist");
        }
    }
}
