/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.strategy.ConnectorConnectionStrategy;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="stripe", friendlyName="Stripe")
public class StripeConnector {
    
    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;
    
    /**
     * Create a Customer
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-customer}
     *
     * @param accountBalance An integer amount in cents that is the starting account balance for your customer. A negative amount represents a credit that will be used before attempting any charges to the customer’s card; a positive amount will be added to the next invoice.
     * @param couponCode If you provide a coupon code, the customer will have a discount applied on all recurring charges. Charges you create through the API will not have the discount.
     * @param description An arbitrary string that you can attach to a customer object. It is displayed alongside the customer in the dashboard.
     * @param email Customer’s email address.
     * @param metadata A set of key/value pairs that you can attach to a customer object. It can be useful for storing additional information about the customer in a structured format.
     * @return Customer created
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Customer createCustomer(@Default("0") int accountBalance, @Optional String couponCode, @Optional String description, @Optional String email, @Optional Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	Map<String, Object> customerParams = new HashMap<String, Object>();
    	if (accountBalance > 0) {
    		customerParams.put("account_balance", accountBalance);
    	}
		customerParams.put("coupon_code", couponCode);
		customerParams.put("description", description);
		customerParams.put("email", email);	
		customerParams.put("metadata", metadata);
		customerParams = removeOptionals(customerParams);
    	try {
			return Customer.create(customerParams);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a customer", e);
		}
    }

    /**
     * Retrieve a Customer
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-customer}
     *
     * @param id The identifier of the customer to be retrieved.
     * @return Returns a customer object if a valid identifier was provided. When requesting the ID of a customer that has been deleted, a subset of the customer's information will be returned, including a "deleted" property, which will be true.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Customer retrieveCustomer(String id) 
    		throws StripeConnectorException {
    	try {
			return Customer.retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the customer", e);
		}
    }
    
    /**
     * Update a Customer. Updates the specified customer by setting the values of the parameters passed. Any parameters not provided will be left unchanged. For example, if you pass the card parameter, that becomes the customer's active card to be used for all charges in the future. When you update a customer to a new valid card: for each of the customer's current subscriptions, if the subscription is in the past_due state, then the latest unpaid, unclosed invoice for the subscription will be retried (note that this retry will not count as an automatic retry, and will not affect the next regularly scheduled payment for the invoice). (Note also that no invoices pertaining to subscriptions in the unpaid state, or invoices pertaining to canceled subscriptions, will be retried as a result of updating the customer's card.) This request accepts mostly the same arguments as the customer creation call.
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-customer}
     *
     * @param id the id of the Customer to update
     * @param accountBalance An integer amount in cents that is the starting account balance for your customer. A negative amount represents a credit that will be used before attempting any charges to the customer’s card; a positive amount will be added to the next invoice.
     * @param couponCode If you provide a coupon code, the customer will have a discount applied on all recurring charges. Charges you create through the API will not have the discount.
     * @param description An arbitrary string that you can attach to a customer object. It is displayed alongside the customer in the dashboard.
     * @param email Customer’s email address.
     * @param metadata A set of key/value pairs that you can attach to a customer object. It can be useful for storing additional information about the customer in a structured format.
     * @param sourceToken A token for a given source
     * @return Customer updated
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Customer updateCustomer(String id, @Default("0") final int accountBalance, @Optional final String couponCode, @Optional final String description, @Optional final String email, @Optional final Map<String, Object> metadata, @Optional final String sourceToken) 
    		throws StripeConnectorException {
    	Map<String, Object> customerParams = new HashMap<String, Object>();
    	if (accountBalance > 0) {
    		customerParams.put("account_balance", accountBalance);
    	}
		customerParams.put("coupon_code", couponCode);
		customerParams.put("description", description);
		customerParams.put("email", email);	
		customerParams.put("metadata", metadata);
		customerParams.put("source", sourceToken);
		customerParams = removeOptionals(customerParams);
    	try {
    		Customer cust = retrieveCustomer(id);
			return cust.update(customerParams);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the customer", e);
		}
    }
    
    /**
     * Delete a Customer
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:delete-customer}
     *
     * @param id The identifier of the customer to be deleted.
     * @return Returns an object with a deleted parameter on success. If the customer ID does not exist, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Object deleteCustomer(String id) 
    		throws StripeConnectorException {
    	try {
			Customer cust = Customer.retrieve(id);
			return cust.delete();
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not delete the customer", e);
		}
    }
    

    /**
     * List all Customers
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-customers}
     *
     * @param limit A limit on the number of records to fetch in a batch
     * @return CustomerCollection of Customers found
     * @throws StripeConnectorException when there is an issue listing customers         
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public CustomerCollection listAllCustomers(@Default("0") int limit) throws StripeConnectorException{
    	Map<String, Object> customerParams = new HashMap<String, Object>();
    	if (limit > 0){
    		customerParams.put("limit", limit);
    	}
    	try {
			return Customer.all(customerParams);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a customer", e);
		}
    }

    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

    private Map<String, Object> removeOptionals(Map<String, Object> map){
    	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (pair.getValue() == null || pair.getValue().toString() == ""){
            	it.remove();
            }             
        }
        return map;
    }

}