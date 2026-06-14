package com.spring_ai.learn_spring_ai.dto;

import com.spring_ai.learn_spring_ai.enums.BookingStatus;

import java.time.Instant;

public record BookingResponse(
        Long bookingId,
        String userId,
        String destination,
        BookingStatus bookingStatus,
        Instant departureTime
) {
}
