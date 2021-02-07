package com.task.tech.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.services.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;


    @Test
    void test_getBookingEstimation_whenParamsAreEmpty_returnsBadRequest() throws Exception {
        mockMvc.perform(
                get("/booking/estimates")
                        .param("economyRoomCount", "")
                        .param("premiumRoomCount", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_getBookingEstimation_whenParamsNotProvided_returnsBadRequest() throws Exception {
        mockMvc.perform(
                get("/booking/estimates"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_getBookingEstimation_whenParamsAreValidAndRatesExists_returnsOK() throws Exception {
        // given
        given(bookingService.getBookingEstimates(any(RoomAvailability.class))).willReturn(new BookingEstimation());

        // then
        mockMvc.perform(
                get("/booking/estimates")
                        .param("economyRoomCount", "3")
                        .param("premiumRoomCount", "3"))
                .andExpect(status().isOk());
    }

    @Test
    void test_saveGuestRates_whenNewRatesExists_returnsCreated() throws Exception {
        // given
        List<Integer> rates = asList(1, 2);

        // then
        mockMvc.perform(
                post("/booking/customerRates").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rates)))
                .andExpect(status().isCreated());
    }

    @Test
    void test_saveGuestRates_whenNewRatesAreEmpty_returnsBadRequest() throws Exception {
        mockMvc.perform(
                post("/booking/customerRates").contentType(APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

}