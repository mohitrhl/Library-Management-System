package com.OpenLeaf.event.publisher;

import com.OpenLeaf.event.PaymentFailedEvent;
import com.OpenLeaf.event.PaymentInitiatedEvent;
import com.OpenLeaf.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishPaymentInitiated(PaymentInitiatedEvent event) {
        log.info("Publishing PaymentInitiatedEvent for payment ID: {}, type: {}",
            event.getPaymentId(), event.getPaymentType());

        applicationEventPublisher.publishEvent(event);

        log.debug("PaymentInitiatedEvent published successfully for payment ID: {}",
            event.getPaymentId());
    }

    public void publishPaymentSuccess(PaymentSuccessEvent event) {
        log.info("Publishing PaymentSuccessEvent for payment ID: {}, type: {}",
            event.getPaymentId(), event.getPaymentType());

        applicationEventPublisher.publishEvent(event);

        log.debug("PaymentSuccessEvent published successfully for payment ID: {}",
            event.getPaymentId());
    }

    public void publishPaymentFailed(PaymentFailedEvent event) {
        log.info("Publishing PaymentFailedEvent for payment ID: {}, type: {}, reason: {}",
            event.getPaymentId(), event.getPaymentType(), event.getFailureReason());

        applicationEventPublisher.publishEvent(event);

        log.debug("PaymentFailedEvent published successfully for payment ID: {}",
            event.getPaymentId());
    }
}
