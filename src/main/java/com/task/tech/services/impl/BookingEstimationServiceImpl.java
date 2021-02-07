package com.task.tech.services.impl;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.dtos.RoomTierBooking;
import com.task.tech.exceptions.EntityNotFoundException;
import com.task.tech.services.BookingEstimationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
public class BookingEstimationServiceImpl implements BookingEstimationService {

    public BookingEstimation getBookingEstimation(RoomAvailability roomAvailability, List<Integer> premiumRates, List<Integer> economyRates) {
        this.validate(premiumRates, economyRates);
        RoomTierBooking premiumBooking = getInitialBookingEstimate(roomAvailability.getPremiumRoomCount(), premiumRates);
        log.debug("premium bookings initially done for {} rooms with {} amount", premiumBooking.getUsage(), premiumBooking.getAmount());
        RoomTierBooking economyBooking = getInitialBookingEstimate(roomAvailability.getEconomyRoomCount(), economyRates);
        log.debug("economy bookings initially done for {} rooms with {} amount", economyBooking.getUsage(), economyBooking.getAmount());
        return getBookingEstimationWithUpgrades(premiumBooking, economyBooking, roomAvailability, economyRates);
    }

    private void validate(List<Integer> premiumRates, List<Integer> economyRates) {
        if (isEmpty(premiumRates) && isEmpty(economyRates)) {
            throw new EntityNotFoundException("no estimates found because customer rates does not exist.");
        }
    }

    private RoomTierBooking getInitialBookingEstimate(Integer availableRoomCount, List<Integer> rates) {

        RoomTierBooking estimation = new RoomTierBooking();
        rates.stream().limit(availableRoomCount).forEach(estimation::bookRoomWithAmount);
        return estimation;
    }

    private BookingEstimation getBookingEstimationWithUpgrades(RoomTierBooking premiumTier, RoomTierBooking economyTier, RoomAvailability roomAvailability, List<Integer> economyRates) {

        int vacantPremiumRooms = roomAvailability.getPremiumRoomCount() - premiumTier.getUsage();
        log.debug("available premium room(s) {} for upgrade", vacantPremiumRooms);
        int totalUpgradeCandidate = economyRates.size() - roomAvailability.getEconomyRoomCount();

        if (vacantPremiumRooms > 0 && totalUpgradeCandidate > 0) {
            int totalUpgrades = 0;
            while (isUpgradePossible(totalUpgrades, vacantPremiumRooms, totalUpgradeCandidate)) {
                Integer currentHighestEconomyRate = economyRates.get(totalUpgrades);
                premiumTier.bookRoomWithAmount(currentHighestEconomyRate);
                economyTier.updateTierBookingAfterUpgrade(currentHighestEconomyRate, totalUpgrades, economyRates);
                totalUpgrades++;
            }
            log.debug("upgraded total {} customers to the premium rooms", totalUpgrades);
        }
        return new BookingEstimation(premiumTier, economyTier);
    }

    private boolean isUpgradePossible(int upgradeCount, int premiumRoomsAvailable, int upgradeCandidateCount) {
        return upgradeCount < premiumRoomsAvailable && upgradeCount < upgradeCandidateCount;
    }
}
