package com.task.tech.dtos;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class RoomAvailability {
    @NotNull
    Integer economyRoomCount;
    @NotNull
    Integer premiumRoomCount;
}
