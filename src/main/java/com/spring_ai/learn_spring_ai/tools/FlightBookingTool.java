package com.spring_ai.learn_spring_ai.tools;

import com.spring_ai.learn_spring_ai.dto.BookingResponse;
import com.spring_ai.learn_spring_ai.enums.BookingStatus;
import com.spring_ai.learn_spring_ai.service.FlightBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightBookingTool {
    private final FlightBookingService flightBookingService;

    @Tool(
            name = "createNewBooking",
            description = "this is used to create the new flight booking for the user with userId"
    )
    public BookingResponse createNewBookings(
       @ToolParam(description = "The userId for which the booking is being made") String userId,
       @ToolParam(description = "the destination of the flight being booked")     String destination,
       @ToolParam(description = "Flight date and time in ISO-8601 UTC format, e.g. 2026-01-14T00:00:00Z")
       Instant departureTime
    )
    {
        return flightBookingService.createNewBookings(userId,destination,departureTime);
    }


    @Tool(
            name = "getAllBookingsForUser",
            description = "this is used to fetch all the bookings for the user with userId"
    )
    public List<BookingResponse> getAllUserBookings(
         @ToolParam(description = "the userId which holds the bookings")   String userId
    ) {
       return flightBookingService.getAllUserBookings(userId);
    }

    @Tool(
            name = "updateBookingStatus",
            description = "this is used to update athe bookings for the user with userId"
    )
    public BookingResponse updateBookingStatus(
          @ToolParam(description = "booking id for which the status is updated")  Long bookingId,
          @ToolParam(description = "the new status that needs to be added to the booking") BookingStatus bookingStatus)
    {
        return flightBookingService.updateBookingStatus(bookingId,bookingStatus);
    }
}
