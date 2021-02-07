package com.task.tech.controllers;

import com.task.tech.RoomOccupancyApplication;
import com.task.tech.dtos.BookingEstimation;
import com.task.tech.services.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {RoomOccupancyApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test_getBookingEstimation_whenRatesNotExist_ReturnsNotFound() {
        // given
        int premiumRoomCount = 3;
        int economyRoomCount = 3;

        ResponseEntity<BookingEstimation> response = restTemplate.exchange(
                createURLWithPort("/estimates?economyRoomCount=" + economyRoomCount + "&premiumRoomCount=" + premiumRoomCount),
                HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BookingEstimation.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void test_getBookingEstimationWhenRatesExists_ReturnsOK() {
        // given
        bookingService.saveGuestRates(asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209));
        int premiumRoomCount = 3;
        int economyRoomCount = 3;

        ResponseEntity<BookingEstimation> response = restTemplate.exchange(
                createURLWithPort("/estimates?economyRoomCount=" + economyRoomCount + "&premiumRoomCount=" + premiumRoomCount),
                HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), BookingEstimation.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookingEstimation bookingEstimation = response.getBody();
        assertEquals(3, bookingEstimation.getPremiumTier().getUsage());
        assertEquals(738, bookingEstimation.getPremiumTier().getAmount());
        assertEquals(3, bookingEstimation.getEconomyTier().getUsage());
        assertEquals(167, bookingEstimation.getEconomyTier().getAmount());
    }

    @Test
    void test_addNewCustomerRates_returnsCreated() {
        // given
        List<Integer> rates = asList(23, 45, 155, 374, 22, 99, 100, 101, 115, 209);

        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/customerRates"), rates, String.class);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/booking" + uri;
    }


}