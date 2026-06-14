package com.spring_ai.learn_spring_ai.repository;

import com.spring_ai.learn_spring_ai.entity.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking,Long> {

    List<FlightBooking> findByUserIdOrderByDepartureTimeDesc(String userId);

    Boolean existsByUserIdAndDepartureTime(String userId, Instant departureTime);
}
