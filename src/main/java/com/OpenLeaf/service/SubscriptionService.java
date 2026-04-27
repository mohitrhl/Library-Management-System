package com.OpenLeaf.service;

import com.OpenLeaf.exception.PaymentException;
import com.OpenLeaf.exception.SubscriptionException;
import com.OpenLeaf.exception.UserException;
import com.OpenLeaf.payload.dto.SubscriptionDTO;
import com.OpenLeaf.payload.request.SubscribeRequest;
import com.OpenLeaf.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SubscriptionService {


    PaymentInitiateResponse subscribe(SubscribeRequest request) throws SubscriptionException, UserException, PaymentException;

    SubscriptionDTO getUsersActiveSubscription(Long userId) throws SubscriptionException, UserException;


    List<SubscriptionDTO> getUserSubscriptions(Long userId) throws SubscriptionException, UserException;

    PaymentInitiateResponse renewSubscription(Long subscriptionId, SubscribeRequest request) throws SubscriptionException, UserException, PaymentException;

    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException;

    SubscriptionDTO getSubscriptionById(Long id) throws SubscriptionException;

    SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException;

    List<SubscriptionDTO> getAllActiveSubscriptions(Pageable pageable);

    void deactivateExpiredSubscriptions();

    boolean hasValidSubscription(Long userId);
}
