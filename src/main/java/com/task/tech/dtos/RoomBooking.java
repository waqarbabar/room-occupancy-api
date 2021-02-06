package com.task.tech.dtos;

import lombok.Data;

@Data
public class RoomBooking {

    private Integer usage;
    private Integer amount;

    public RoomBooking() {
        this.usage = 0;
        this.amount = 0;
    }

    public void bookRoomWithAmount(Integer amount) {
        usage += 1;
        this.amount += amount;
    }

    public void cancelBookingWithAmount(Integer amount) {
        usage -= 1;
        this.amount -= amount;
    }
}
