/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

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
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;
import org.slf4j.LoggerFactory;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Balance;


/**
 * Connection Management Strategy
 *
 * @author WhiteSky Labs
 */
@ConnectionManagement(configElementName = "config-type", friendlyName = "Connection Management type strategy")
public class ConnectorConnectionStrategy {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConnectorConnectionStrategy.class);
	
    @Configurable
    @Default("2015-03-24")
    private String apiVersion;
    
    /**
     * @param apiKey
     */
    private static void staticSetApiKey(String apiKey){
    	Stripe.apiKey = apiKey;
    }
    
    /**
     * @param apiVersion
     */
    private static void staticSetApiVersion(String apiVersion){
    	Stripe.apiVersion = apiVersion;
    }
    
    /**
     * Connect
     *
     * @param apiKey Your Stripe API Key
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey final String apiKey)
        throws ConnectionException {
    	
        staticSetApiKey(apiKey);
        staticSetApiVersion(this.apiVersion);
        try {
			Balance.retrieve();
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			LOGGER.error("Error connecting to Stripe", e);
			throw new ConnectionException(
		             ConnectionExceptionCode.INCORRECT_CREDENTIALS, 
		             e.getMessage(),
		             "Unable to Connect to Stripe");
		}     	
        //if we manage to get here, it means that the connection was 
        // successful, hence, no need to return a boolean           
    }

    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        staticSetApiKey(null);
    }

    /**
     * Are we connected
     */
    @ValidateConnection
    public boolean isConnected() {
        return Stripe.apiKey != null;
    }

    /**
     * The Connection Identifier
     * For now, we are using the Stripe SDK Version, arbitrarily     
     * @return The Connection Identifier
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