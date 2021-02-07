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

    public void updateTierBookingAfterUpgrade(Integer oldRate, Integer totalUpgradesCount, List<Integer> rates) {
        if (this.isUpdatePossible()) {
            this.vacateRoomWithAmount(oldRate);
            int totalBookingCount = this.usage + totalUpgradesCount;
            Integer nextAvailableRate = getNextUnusedRate(totalBookingCount, rates);
            if (nonNull(nextAvailableRate)) {
                this.bookRoomWithAmount(nextAvailableRate);
            }
        }
    }

    private boolean isUpdatePossible() {
        return this.usage > 0;
    }

    private Integer getNextUnusedRate(int totalBookingCount, List<Integer> rates) {
        if (isNextRateAvailable(rates.size(), totalBookingCount)) {
            return rates.get(totalBookingCount + 1);
        }
        return null;
    }

    private boolean isNextRateAvailable(int rateCount, int totalBookingCount) {
        return rateCount > totalBookingCount;
    }
}
