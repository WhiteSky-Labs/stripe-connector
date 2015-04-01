/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe;

import java.util.HashMap;
import java.util.Map;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;

import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
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
     * List all Customers
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-customers}
     *
     * @param limit A limit on the number of records to fetch in a batch
     * @return CustomerCollection of Customers found
     * @throws Exception Comment for Exception     
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public CustomerCollection listAllCustomers(@Default("0") int limit /*@Optional Customer startingAfter, @Optional Customer endingBefore*/) throws Exception {
    	Map<String, Object> customerParams = new HashMap<String, Object>();
    	if (limit > 0){
    		customerParams.put("limit", limit);
    	}
    	/*if (startingAfter != null) {
    		customerParams.put("starting_after", startingAfter);    			
    	}
    	if (endingBefore != null) {
    		customerParams.put("ending_before", endingBefore);
    	}*/
    	return Customer.all(customerParams);
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