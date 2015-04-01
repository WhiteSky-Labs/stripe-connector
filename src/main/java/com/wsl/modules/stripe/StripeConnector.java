/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Configurable;

import org.mule.api.annotations.Processor;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.ReconnectOn;

import org.mule.api.annotations.param.Default;

import com.wsl.modules.stripe.strategy.ConnectorConnectionStrategy;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="stripe", friendlyName="Stripe")
public class StripeConnector {
    
    /**
     * Configurable
     */
    @Configurable
    @Default("Hello")
    private String greeting;

    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;

    /**
     * Custom processor
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:greet}
     *
     * @param friend Content to be processed
     * @return Some string
     * @throws Exception Comment for Exception     
     */
    @Processor
    @OAuthProtected
    @ReconnectOn(exceptions = { Exception.class })
    public String greet(@Default("friend") String friend) throws Exception {
        /*
         * MESSAGE PROCESSOR CODE GOES HERE
         */
        return greeting + " " + friend + ". " + connectionStrategy.getReplay();
    }

    /**
     * Set property
     *
     * @param greeting My property
     */
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    /**
     * Get property
     */
    public String getGreeting() {
        return this.greeting;
    }

    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

}