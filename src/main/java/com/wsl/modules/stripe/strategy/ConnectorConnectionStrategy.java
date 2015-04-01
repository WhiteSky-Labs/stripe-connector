/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe.strategy;

import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Balance;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;

/**
 * Connection Management Strategy
 *
 * @author WhiteSky Labs
 */
@ConnectionManagement(configElementName = "config-type", friendlyName = "Connection Management type strategy")
public class ConnectorConnectionStrategy {
    
    @Configurable
    @Default("2015-03-24")
    private String apiVersion;
    
    /**
     * Connect
     *
     * @param apiKey Your Stripe API Key
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey String apiKey)
        throws ConnectionException {
        Stripe.apiKey = apiKey;
        Stripe.apiVersion = this.apiVersion;
        try {
			Balance balance = Balance.retrieve();
		} catch (AuthenticationException e) {
			throw new ConnectionException(
		             ConnectionExceptionCode.INCORRECT_CREDENTIALS, 
		             e.getMessage(),
		             "Your API Key is probably incorrect");
		} catch (InvalidRequestException e) {
			throw new ConnectionException(
		             ConnectionExceptionCode.UNKNOWN, 
		             e.getMessage(),
		             "The request was invalid. Your API version might be incorrect.");
		} catch (APIConnectionException e) {
			throw new ConnectionException(
		             ConnectionExceptionCode.CANNOT_REACH, 
		             e.getMessage(),
		             "Could not get a connection to Stripe. Check your API Version and Connectivity.");
		} catch (CardException e) {
			throw new ConnectionException(
		             ConnectionExceptionCode.UNKNOWN, 
		             e.getMessage(),
		             "A Card Error shouldn't happen here. Check your API Version.");
		} catch (APIException e) {
			throw new ConnectionException(
		             ConnectionExceptionCode.CANNOT_REACH, 
		             e.getMessage(),
		             "There was an issue with the API, check your API Version and Key");
		}    	
        //if we manage to get here, it means that the connection was 
        // successful, hence, no need to return a boolean           
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        Stripe.apiKey = null;
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        if (Stripe.apiKey != null) {
        	return true;
        } else {
        	return false;
        }
    }

    /**
     * The Connection Identifier
     * For now, we are using the Stripe SDK Version, arbitrarily
     */
    @ConnectionIdentifier
    public String connectionId() {
        return Stripe.VERSION;
    }

    /**
     * Set API Version
     *
     * @param apiVersion The API Version 
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Get configured API Version
     */
    public String getApiVersion() {
        return this.apiVersion;
    }
}