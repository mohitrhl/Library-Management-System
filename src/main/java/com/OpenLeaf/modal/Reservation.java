package com.OpenLeaf.modal;

import com.OpenLeaf.domain.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_book_id", columnList = "book_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_reserved_at", columnList = "reserved_at"),
    @Index(name = "idx_available_until", columnList = "available_until")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is mandatory for reservation")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @NotNull(message = "Book is mandatory for reservation")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


    @NotNull(message = "Reservation status is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;


    @NotNull
    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Column(name = "available_at")
    private LocalDateTime availableAt;

    @Column(name = "available_until")
    private LocalDateTime availableUntil;

    @Column(name = "fulfilled_at")
    private LocalDateTime fulfilledAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "queue_position")
    private Integer queuePosition;

    @Column(name = "notification_sent", nullable = false)
    private Boolean notificationSent = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return status == ReservationStatus.PENDING || status == ReservationStatus.AVAILABLE;
    }

    public boolean canBeCancelled() {
        return status == ReservationStatus.PENDING || status == ReservationStatus.AVAILABLE;
    }

    public boolean hasExpired() {
        return status == ReservationStatus.AVAILABLE
            && availableUntil != null
            && LocalDateTime.now().isAfter(availableUntil);
    }
}
