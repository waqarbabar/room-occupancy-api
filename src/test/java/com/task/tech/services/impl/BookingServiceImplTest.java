package com.task.tech.services.impl;

import com.task.tech.dtos.RoomAvailability;
import com.task.tech.exceptions.EntityNotFoundException;
import com.task.tech.services.BookingEstimationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.google.common.primitives.Ints.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BookingServiceImplTest {

    @Captor
    private ArgumentCaptor<List<Integer>> ratesCaptor;
    @Mock
    private BookingEstimationService bookingEstimationService;
    @InjectMocks
    private BookingServiceImpl serviceUnderTest;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.initMocks(this);
        serviceUnderTest.setPremiumMinimumThreshold(100);
    }

    @Test
    void test_getBookingEstimates_whenRatesProvided_callsInternalServiceOnceWithProperPremiumRatesData() {
        // given
        serviceUnderTest.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(5, 5);

        // when
        serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        verify(bookingEstimationService, times(1)).getBookingEstimation(any(), ratesCaptor.capture(), anyList());
        assertEquals(asList(374, 209, 155, 115, 101, 100), ratesCaptor.getValue());
    }

    @Test
    void test_getBookingEstimates_whenRatesProvided_callsInternalServiceOnceWithProperEconomyRatesData() {
        // given
        serviceUnderTest.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(5, 5);

        // when
        serviceUnderTest.getBookingEstimates(roomAvailabilityDTO);

        // then
        verify(bookingEstimationService, times(1)).getBookingEstimation(any(), anyList(), ratesCaptor.capture());
        assertEquals(asList(99, 45, 23, 22), ratesCaptor.getValue());
    }

    @Test
    void test_getBookingEstimates_whenNoRatesProvided_throwsException() {
        // given
        RoomAvailability roomAvailabilityDTO = new RoomAvailability(5, 5);

        // then
        assertThrows(EntityNotFoundException.class, () -> serviceUnderTest.getBookingEstimates(roomAvailabilityDTO));
        verify(bookingEstimationService, never()).getBookingEstimation(any(), anyList(), anyList());
    }
}
