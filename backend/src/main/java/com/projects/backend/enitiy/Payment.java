package com.projects.backend.enitiy;

import com.projects.backend.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "booking_id",
            unique = true
    )
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;

    private String transactionId;
}
