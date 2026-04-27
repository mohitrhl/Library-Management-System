package com.OpenLeaf.controller;

import com.OpenLeaf.exception.PaymentException;
import com.OpenLeaf.payload.dto.PaymentDTO;
import com.OpenLeaf.payload.request.PaymentInitiateRequest;
import com.OpenLeaf.payload.request.PaymentVerifyRequest;
import com.OpenLeaf.payload.response.ApiResponse;
import com.OpenLeaf.payload.response.PaymentInitiateResponse;
import com.OpenLeaf.payload.response.RevenueStatisticsResponse;
import com.OpenLeaf.service.PaymentService;
import com.OpenLeaf.service.impl.PaymentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentServiceImpl paymentServiceImpl;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@Valid @RequestBody PaymentInitiateRequest request) {
        try {
            PaymentInitiateResponse response = paymentService.initiatePayment(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequest request) {
        try {
            PaymentDTO payment = paymentService.verifyPayment(request);
            return ResponseEntity.ok(payment);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        try {
            PaymentDTO payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(payment);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> getPaymentByTransactionId(@PathVariable String transactionId) {
        try {
            PaymentDTO payment = paymentService.getPaymentByTransactionId(transactionId);
            return ResponseEntity.ok(payment);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPayments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<PaymentDTO> payments = paymentService.getUserPayments(userId, pageable);
            return ResponseEntity.ok(payments);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PaymentDTO> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(payments);
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Long id) {
        try {
            PaymentDTO payment = paymentService.cancelPayment(id);
            return ResponseEntity.ok(payment);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PostMapping("/{id}/retry")
    public ResponseEntity<?> retryPayment(@PathVariable Long id) {
        try {
            PaymentInitiateResponse response = paymentService.retryPayment(id);
            return ResponseEntity.ok(response);
        } catch (PaymentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PostMapping("/webhook/razorpay")
    public ResponseEntity<String> handleRazorpayWebhook(@RequestBody String webhookBody) {
        try {
            log.info("Received Razorpay webhook");

            // Parse webhook payload
            JSONObject webhookPayload = new JSONObject(webhookBody);

            // Process webhook
            paymentServiceImpl.handleRazorpayWebhook(webhookPayload);

            log.info("Razorpay webhook processed successfully");
            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            log.error("Error processing Razorpay webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Webhook processing failed");
        }
    }


    @GetMapping("/statistics/monthly-revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueStatisticsResponse> getMonthlyRevenue() {
        RevenueStatisticsResponse stats = paymentService.getMonthlyRevenue();
        return ResponseEntity.ok(stats);
    }


}
