package com.OpenLeaf.modal;

import com.OpenLeaf.domain.PaymentGateway;
import com.OpenLeaf.domain.PaymentStatus;
import com.OpenLeaf.domain.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_book_loan_id", columnList = "book_loan_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_payment_type", columnList = "payment_type"),
    @Index(name = "idx_gateway", columnList = "gateway"),
    @Index(name = "idx_transaction_id", columnList = "transaction_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is mandatory for payment")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_loan_id")
    private BookLoan bookLoan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    private Fine fine;

    @NotNull(message = "Payment type is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 30)
    private PaymentType paymentType;

    @NotNull(message = "Payment status is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @NotNull(message = "Payment gateway is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentGateway gateway;


    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    private Long amount;


    @Column(length = 3, nullable = false)
    private String currency = "INR";


    @Column(name = "transaction_id", length = 255)
    private String transactionId;


    @Column(name = "gateway_payment_id", length = 255)
    private String gatewayPaymentId;

    @Column(name = "gateway_order_id", length = 255)
    private String gatewayOrderId;


    @Column(name = "gateway_signature", length = 512)
    private String gatewaySignature;


    @Column(name = "payment_method", length = 50)
    private String paymentMethod;


    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(columnDefinition = "TEXT")
    private String description;


    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;


    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;


    @Column(name = "initiated_at", nullable = false)
    private LocalDateTime initiatedAt;


    @Column(name = "completed_at")
    private LocalDateTime completedAt;


    @Column(name = "notification_sent", nullable = false)
    private Boolean notificationSent = false;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCESS;
    }

    public boolean canRetry() {
        return (status == PaymentStatus.FAILED || status == PaymentStatus.CANCELLED)
               && retryCount < 3;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING || status == PaymentStatus.PROCESSING;
    }
}
