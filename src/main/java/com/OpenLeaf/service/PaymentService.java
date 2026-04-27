package com.OpenLeaf.service;

import com.OpenLeaf.exception.PaymentException;
import com.OpenLeaf.payload.dto.PaymentDTO;
import com.OpenLeaf.payload.request.PaymentInitiateRequest;
import com.OpenLeaf.payload.request.PaymentVerifyRequest;
import com.OpenLeaf.payload.response.PaymentInitiateResponse;
import com.OpenLeaf.payload.response.RevenueStatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PaymentService {


    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws PaymentException;

    PaymentDTO verifyPayment(PaymentVerifyRequest request) throws PaymentException;

    PaymentDTO getPaymentById(Long paymentId) throws PaymentException;

    PaymentDTO getPaymentByTransactionId(String transactionId) throws PaymentException;

    Page<PaymentDTO> getUserPayments(Long userId, Pageable pageable) throws PaymentException;

    Page<PaymentDTO> getAllPayments(Pageable pageable);

    PaymentDTO cancelPayment(Long paymentId) throws PaymentException;

    PaymentInitiateResponse retryPayment(Long paymentId) throws PaymentException;

    RevenueStatisticsResponse getMonthlyRevenue();
}
