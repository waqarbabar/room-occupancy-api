package com.task.tech.services.impl;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.dtos.RoomBooking;
import com.task.tech.exceptions.EntityNotFoundException;
import com.task.tech.services.BookingService;
import lombok.extern.slf4j.Slf4j;
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

    private static final Integer PREMIUM_MINIMUM_THRESHOLD = 100;
    private final List<Integer> guestRates;

    public BookingServiceImpl() {
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
        List<Integer> economyRates = sortedRatesMap.get(false);
        return getBookingEstimation(roomAvailability, premiumRates, economyRates);
    }

    private BookingEstimation getBookingEstimation(RoomAvailability roomAvailability, List<Integer> premiumRates, List<Integer> economyRates) {
        BookingEstimation bookingEstimationDTO = new BookingEstimation();
        RoomBooking premiumBooking = getInitialEstimate(roomAvailability.getPremiumRoomCount(), premiumRates);
        RoomBooking economyBooking = getInitialEstimate(roomAvailability.getEconomyRoomCount(), economyRates);
        this.processUpgrade(premiumBooking, economyBooking, roomAvailability, economyRates);
        bookingEstimationDTO.setEconomyBooking(economyBooking);
        bookingEstimationDTO.setPremiumBooking(premiumBooking);
        return bookingEstimationDTO;
    }

    private void processUpgrade(RoomBooking premiumBooking, RoomBooking economyBooking, RoomAvailability roomAvailability, List<Integer> economyRates) {
        int premiumRoomLeft = roomAvailability.getPremiumRoomCount() - premiumBooking.getUsage();
        log.debug("available {} premium room(s) for upgrade", premiumRoomLeft);
        int upgradeCandidateCount = economyRates.size() - economyBooking.getUsage();

        if (premiumRoomLeft == 0 || upgradeCandidateCount == 0) {
            log.debug("no upgrade possible, continuing with the initial estimation");
            return;
        }

        for (int i = 0; (i < premiumRoomLeft && i < upgradeCandidateCount && i < economyRates.size()); i++) {
            Integer currentRate = economyRates.get(i);
            premiumBooking.bookRoomWithAmount(currentRate);
            if (economyBooking.getUsage() > 0) {
                economyBooking.cancelBookingWithAmount(currentRate);
                if (economyRates.size() > economyBooking.getUsage() + i) {
                    economyBooking.bookRoomWithAmount(economyRates.get(economyBooking.getUsage() + i + 1));
                }
            }
        }
    }

    private Map<Boolean, List<Integer>> getSortedRates() {
        return guestRates
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(partitioningBy(offeredRate -> offeredRate >= PREMIUM_MINIMUM_THRESHOLD));
    }

    private RoomBooking getInitialEstimate(Integer availableRoomCount, List<Integer> rates) {
        RoomBooking estimation = new RoomBooking();
        rates.stream().limit(availableRoomCount).forEach(estimation::bookRoomWithAmount);
        return estimation;
    }

    private void validate() {
        if (isEmpty(guestRates)) {
            throw new EntityNotFoundException("No estimates found because customer rates does not exist.");
        }
    }
}
