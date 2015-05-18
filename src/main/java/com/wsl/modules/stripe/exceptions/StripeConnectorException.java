/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

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