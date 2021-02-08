package com.task.tech.dtos;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

import static java.util.Objects.nonNull;

@Getter
@ToString
public class RoomTierBooking {

    private Integer usage;
    private Integer amount;

    public RoomTierBooking() {
        this.usage = 0;
        this.amount = 0;
    }

    public synchronized void bookRoomWithAmount(Integer amount) {
        usage += 1;
        this.amount += amount;
    }

    public synchronized void vacateRoomWithAmount(Integer amount) {
        if (this.usage > 0) {
            usage -= 1;
            this.amount -= amount;
        }
    }

    public void updateTierBookingAfterUpgrade(Integer oldRate, Integer totalUpgrades, List<Integer> currentTierRates) {
        if (this.isUpdatePossible()) {
            this.vacateRoomWithAmount(oldRate);
            int currentTierBookingCount = this.usage + totalUpgrades;
            Integer nextAvailableRate = getNextUnusedRate(currentTierBookingCount, currentTierRates);
            if (nonNull(nextAvailableRate)) {
                this.bookRoomWithAmount(nextAvailableRate);
            }
        }
    }

    private boolean isUpdatePossible() {
        return this.usage > 0;
    }

    private Integer getNextUnusedRate(int currentTierBookingCount, List<Integer> currentTierRates) {
        if (isNextRateAvailable(currentTierRates.size(), currentTierBookingCount)) {
            return currentTierRates.get(currentTierBookingCount + 1);
        }
        return null;
    }

    private boolean isNextRateAvailable(int totalCurrentTierRates, int currentTierBookingCount) {
        return totalCurrentTierRates > currentTierBookingCount;
    }
}
