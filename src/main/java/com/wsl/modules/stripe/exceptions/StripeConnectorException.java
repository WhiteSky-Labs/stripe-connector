package com.wsl.modules.stripe.exceptions;

/**
 * Exception thrown by the connector when we cannot resolve it to {@link com.wsl.modules.stripe.exceptions.StripeConnectorException}
 */
public class StripeConnectorException extends Exception {
	 /**
     * Create a SplunkConnectorException
     *
     * @param message The human-readable message
     * @param cause   The underlying exception
     */
    public StripeConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
