package com.task.tech.controllers;

import com.task.tech.dtos.BookingEstimation;
import com.task.tech.dtos.RoomAvailability;
import com.task.tech.services.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.task.tech.controllers.BookingController.TAG_API;

@Slf4j
@SwaggerDefinition(
        consumes = {"application/json"},
        produces = {"application/json"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = @Tag(name = TAG_API, description = TAG_API))
@Api(value = TAG_API, tags = TAG_API)
@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    public static final String TAG_API = "Room Occupancy API";
    private final BookingService bookingService;

    @ApiOperation(httpMethod = "GET", value = "Get booking estimates of different room category", nickname = "getBookingEstimation",
            notes = "get Booking Estimation", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping("/estimates")
    public BookingEstimation getBookingEstimation(@Valid RoomAvailability roomAvailability) {
        log.info("received request to get booking estimations for available rooms={}", roomAvailability);
        BookingEstimation bookingEstimates = this.bookingService.getBookingEstimates(roomAvailability);
        log.info("completed request to get booking estimations with details={}", bookingEstimates);
        return bookingEstimates;
    }

    @ApiOperation(httpMethod = "POST", value = "Add new customer rates", nickname = "addNewCustomerRates",
            notes = "Add new customer rates", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @PostMapping("/customerRates")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewCustomerRates(@RequestBody List<Integer> customerRates) {
        log.info("received request to add new customer rates={}", customerRates);
        this.bookingService.saveGuestRates(customerRates);
        log.info("completed request to add new customer rates");
    }
}
