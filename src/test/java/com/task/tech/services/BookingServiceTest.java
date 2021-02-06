package com.task.tech.services;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.exceptions.EntityNotFoundException;
import com.task.tech.services.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingServiceTest {

    private BookingService serviceUnderTest;

    @BeforeEach
    void beforeEach() {
        serviceUnderTest = new BookingServiceImpl();
    }

    @Test
    void test_getBookingEstimates_whenLessEconomyAndLessPremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        serviceUnderTest.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(3, 3);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        assertEquals(3, bookingEstimation.getPremiumBooking().getUsage());
        assertEquals(738, bookingEstimation.getPremiumBooking().getAmount());
        assertEquals(3, bookingEstimation.getEconomyBooking().getUsage());
        assertEquals(167, bookingEstimation.getEconomyBooking().getAmount());
    }

    @Test
    void test_getBookingEstimates_whenMoreEconomyAndMorePremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        serviceUnderTest.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(5, 7);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        assertEquals(6, bookingEstimation.getPremiumBooking().getUsage());
        assertEquals(1054, bookingEstimation.getPremiumBooking().getAmount());
        assertEquals(4, bookingEstimation.getEconomyBooking().getUsage());
        assertEquals(189, bookingEstimation.getEconomyBooking().getAmount());
    }

    @Test
    void test_getBookingEstimates_whenMoreEconomyAndLessPremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        serviceUnderTest.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(7, 2);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        assertEquals(2, bookingEstimation.getPremiumBooking().getUsage());
        assertEquals(583, bookingEstimation.getPremiumBooking().getAmount());
        assertEquals(4, bookingEstimation.getEconomyBooking().getUsage());
        assertEquals(189, bookingEstimation.getEconomyBooking().getAmount());
    }

    @Test
    void test_getBookingEstimates_whenLessEconomyAndOneMorePremiumRoomsAvailable_ReturnsResultWithOneUpgrades() {
        // given
        serviceUnderTest.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(1, 7);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        assertEquals(7, bookingEstimation.getPremiumBooking().getUsage());
        assertEquals(1153, bookingEstimation.getPremiumBooking().getAmount());
        assertEquals(1, bookingEstimation.getEconomyBooking().getUsage());
        assertEquals(45, bookingEstimation.getEconomyBooking().getAmount());
    }

    @Test
    void test_getBookingEstimates_whenLessEconomyAndTwoMorePremiumRoomsAvailable_ReturnsResultWithTwoUpgrades() {
        // given
        serviceUnderTest.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(2, 8);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        assertEquals(8, bookingEstimation.getPremiumBooking().getUsage());
        assertEquals(1198, bookingEstimation.getPremiumBooking().getAmount());
        assertEquals(2, bookingEstimation.getEconomyBooking().getUsage());
        assertEquals(45, bookingEstimation.getEconomyBooking().getAmount());
    }

    @Test
    void test_getBookingEstimates_whenNoEconomyAnAllPremiumRoomsAvailable_ReturnsResultWithFourUpgrades() {
        // given
        serviceUnderTest.saveGuestRates(asList(1, 3, 400, 500, 2, 4, 100, 200, 600, 300));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(0, 10);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        assertEquals(10, bookingEstimation.getPremiumBooking().getUsage());
        assertEquals(2110, bookingEstimation.getPremiumBooking().getAmount());
        assertEquals(0, bookingEstimation.getEconomyBooking().getUsage());
        assertEquals(0, bookingEstimation.getEconomyBooking().getAmount());
    }

    @Test
    void test_getBookingEstimates_whenAllEconomyAnNoPremiumRoomsAvailable_ReturnsResultWithNoUpgrades() {
        // given
        serviceUnderTest.saveGuestRates(asList(1, 3, 400, 500, 2, 4, 100, 200, 600, 300));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(10, 0);

        // when
        BookingEstimation bookingEstimation = serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        assertEquals(0, bookingEstimation.getPremiumBooking().getUsage());
        assertEquals(0, bookingEstimation.getPremiumBooking().getAmount());
        assertEquals(4, bookingEstimation.getEconomyBooking().getUsage());
        assertEquals(10, bookingEstimation.getEconomyBooking().getAmount());
    }

    @Test
    void test_getBookingEstimates_whenNoRatesProvided_throwsException() {
        // given
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(5, 5);

        // then
        assertThrows(EntityNotFoundException.class, () -> serviceUnderTest.getBookingEstimates(roomAvailabilityDTO));
    }
}