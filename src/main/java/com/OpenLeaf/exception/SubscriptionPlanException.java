package com.OpenLeaf.exception;

public class SubscriptionPlanException extends Exception {

    public SubscriptionPlanException(String message) {
        super(message);
    }

    public SubscriptionPlanException(String message, Throwable cause) {
        super(message, cause);
    }
}
