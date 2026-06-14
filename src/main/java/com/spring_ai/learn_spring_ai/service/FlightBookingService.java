package com.spring_ai.learn_spring_ai.service;

import com.spring_ai.learn_spring_ai.dto.BookingResponse;
import com.spring_ai.learn_spring_ai.entity.FlightBooking;
import com.spring_ai.learn_spring_ai.enums.BookingStatus;
import com.spring_ai.learn_spring_ai.repository.FlightBookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class FlightBookingService {
    private final FlightBookingRepository flightBookingRepository;

    public List<BookingResponse> getAllUserBookings(String userId)
    {
        List<FlightBooking> flightBookings =flightBookingRepository.findByUserIdOrderByDepartureTimeDesc(userId);
        return flightBookings.stream()
                .map(flightBooking -> new BookingResponse(
                        flightBooking.getId(),
                        flightBooking.getUserId(),
                        flightBooking.getDestination(),
                        flightBooking.getBookingStatus(),
                        flightBooking.getDepartureTime()
                ))
                .collect(Collectors.toList());
    }

    public BookingResponse createNewBookings(String userId, String destination, Instant departureTime)
    {
        Boolean flightBooking=flightBookingRepository.existsByUserIdAndDepartureTime(userId,departureTime);
        if(flightBooking)
        {
            throw new RuntimeException("Booking already exists for the depatureTime");
        }

        FlightBooking flightBooking1 = FlightBooking.builder()
                .destination(destination)
                .userId(userId)
                .bookingStatus(BookingStatus.CONFIRMED)
                .departureTime(departureTime)
                .build();

        flightBookingRepository.save(flightBooking1);

        return new BookingResponse(
                flightBooking1.getId(),
                flightBooking1.getUserId(),
                flightBooking1.getDestination(),
                flightBooking1.getBookingStatus(),
                flightBooking1.getDepartureTime()
        );
    }

    public BookingResponse updateBookingStatus(Long bookingId,BookingStatus bookingStatus)
    {
        FlightBooking flightBooking = flightBookingRepository.findById(bookingId)
                .orElseThrow(()-> new RuntimeException("Booking not found"));
        flightBooking.setBookingStatus(bookingStatus);

        flightBookingRepository.save(flightBooking);
        return new BookingResponse(
                flightBooking.getId(),
                flightBooking.getUserId(),
                flightBooking.getDestination(),
                flightBooking.getBookingStatus(),
                flightBooking.getDepartureTime()
        );


    }


}
