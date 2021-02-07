package com.task.tech.services.impl;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.exceptions.EntityNotFoundException;
import com.task.tech.services.BookingEstimationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingEstimationServiceImplTest {

    protected BookingEstimationService serviceUnderTest;

    @BeforeEach
    void beforeEach() {
        this.serviceUnderTest = new BookingEstimationServiceImpl();
    }

    @Test
    void test_getBookingEstimation_whenLessEconomyAndLessPremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        List<Integer> economyRates = asList(99, 45, 23, 22);
        List<Integer> premiumRates = asList(374, 209, 155, 115, 101, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(3, 3);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(3, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(738, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(3, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(167, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenMoreEconomyAndMorePremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        List<Integer> economyRates = asList(99, 45, 23, 22);
        List<Integer> premiumRates = asList(374, 209, 155, 115, 101, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(5, 7);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(6, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(1054, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(4, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(189, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenMoreEconomyAndLessPremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        List<Integer> economyRates = asList(99, 45, 23, 22);
        List<Integer> premiumRates = asList(374, 209, 155, 115, 101, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(7, 2);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(2, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(583, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(4, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(189, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenLessEconomyAndOneMorePremiumRoomsAvailable_ReturnsResultWithOneUpgrades() {
        // given
        List<Integer> economyRates = asList(99, 45, 23, 22);
        List<Integer> premiumRates = asList(374, 209, 155, 115, 101, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(1, 7);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(7, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(1153, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(1, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(45, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenLessEconomyAndTwoMorePremiumRoomsAvailable_ReturnsResultWithTwoUpgrades() {
        // given
        List<Integer> economyRates = asList(99, 45, 23, 22);
        List<Integer> premiumRates = asList(374, 209, 155, 115, 101, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(2, 8);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(8, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(1198, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(2, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(45, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenMoreEconomyAnMorePremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        List<Integer> economyRates = asList(4, 3, 2, 1);
        List<Integer> premiumRates = asList(600, 500, 400, 300, 200, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(20, 20);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(6, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(2100, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(4, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(10, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenNoEconomyAnAllPremiumRoomsAvailable_ReturnsResultWithFourUpgrades() {
        // given
        List<Integer> economyRates = asList(4, 3, 2, 1);
        List<Integer> premiumRates = asList(600, 500, 400, 300, 200, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(0, 10);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(10, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(2110, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(0, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(0, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenAllEconomyAnNoPremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        List<Integer> economyRates = asList(4, 3, 2, 1);
        List<Integer> premiumRates = asList(600, 500, 400, 300, 200, 100);
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(10, 0);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, premiumRates, economyRates);

        // then
        assertEquals(0, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(0, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(4, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(10, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_getBookingEstimation_whenEconomyAndPremiumRatesEmpty_throwException() {
        // given
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(10, 0);

        // when
        assertThrows(EntityNotFoundException.class, () -> serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, emptyList(), emptyList()));
    }

    @Test
    void test_getBookingEstimation_whenEconomyAndPremiumRatesNull_throwException() {
        // given
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(10, 0);

        // when
        assertThrows(EntityNotFoundException.class, () -> serviceUnderTest.getBookingEstimation(roomAvailabilityDTO, null, null));
    }
}