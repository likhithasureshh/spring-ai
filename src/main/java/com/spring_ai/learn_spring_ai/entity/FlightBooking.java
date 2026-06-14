package com.spring_ai.learn_spring_ai.entity;

import com.spring_ai.learn_spring_ai.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userId;
    Instant departureTime;
    String destination;
    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;
    @CreationTimestamp
    Instant bookedAt;

}
