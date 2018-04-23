/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe.client;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Subscription;
import com.wsl.modules.stripe.complextypes.Source;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Subscription-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeSubscriptionClient {
    /**
     * Create a Subscription
     * 
     * @param customerId The ID of the customer to create the subscription on
     * @param plan The identifier of the plan to subscribe the customer to.
     * @param coupon The code of the coupon to apply to this subscription. A coupon applied to a subscription will only affect invoices created for that particular subscription.
     * @param trialEnd Unix timestamp representing the end of the trial period the customer will get before being charged for the first time. If set, trial_end will override the default trial period of the plan the customer is being subscribed to. The special value now can be provided to end the customer's trial immediately.
     * @param sourceToken The source can either be a token, like the ones returned by our Stripe.js, ...
     * @param source ...or a Map containing a user's credit card details (with the options shown below). You must provide a source if the customer does not already have a valid source attached, and you are subscribing the customer for a plan that is not free. Passing source will create a new source object, make it the customer default source, and delete the old customer default if one exists. If you want to add an additional source to use with subscriptions, instead use the card creation API to add the card and then the customer update API to set it as the default. Whenever you attach a card to a customer, Stripe will automatically validate the card.
     * @param quantity The quantity you'd like to apply to the subscription you're creating. For example, if your plan is $10/user/month, and your customer has 5 users, you could pass 5 as the quantity to have the customer charged $50 (5 x $10) monthly. If you update a subscription but don't change the plan ID (e.g. changing only the trial_end), the subscription will inherit the old subscription's quantity attribute unless you pass a new quantity parameter. If you update a subscription and change the plan ID, the new subscription will not inherit the quantity attribute and will default to 1 unless you pass a quantity parameter.
     * @param applicationFeePercent A positive decimal (with at most two decimal places) between 1 and 100. This represents the percentage of the subscription invoice subtotal that will be transferred to the application owner’s Stripe account. The request must be made with an OAuth key in order to set an application fee percentage. For more information, see the application fees documentation.
     * @param taxPercent A positive decimal (with at most two decimal places) between 1 and 100. This represents the percentage of the subscription invoice subtotal that will be calculated and added as tax to the final amount each billing period. For example, a plan which charges $10/month with a tax_percent of 20.0 will charge $12 per invoice.
     * @param metadata A set of key/value pairs that you can attach to a subscription object. It can be useful for storing additional information about the subscription in a structured format.
     * @return Returns the newly created subscription object if the call succeeded. If the customer has no card or the attempted charge fails, this call throws an error (unless the specified plan is free or has a trial period).
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Subscription createSubscription(String customerId, String plan, String coupon, String trialEnd, String sourceToken, Source source, int quantity, double applicationFeePercent, double taxPercent, Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	params.put("plan", plan);
		params.put("coupon", coupon);
		params.put("trial_end", trialEnd);
		if (sourceToken != null && !sourceToken.isEmpty()){
			params.put("source", sourceToken);
		} else if (source != null){
			params.put("source", StripeClientUtils.removeOptionals(source.toDictionary()));
		}
		params.put("quantity", quantity);
		params.put("application_fee_percent", applicationFeePercent);
		params.put("tax_percent", taxPercent);
		params.put("metadata", metadata);
		params = StripeClientUtils.removeOptionalsAndZeroes(params);
		
    	try {
			Customer customer = Customer.retrieve(customerId);
			return customer.createSubscription(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the Subscription", e);
		}
    }
    
    /**
     * Retrieve a Subscription
     * 
     * @param customerId The ID of the customer to create the subscription on
     * @param subscriptionId ID of subscription to retrieve.
     * @return Returns the subscription object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Subscription retrieveSubscription(String customerId, String subscriptionId)    
    		throws StripeConnectorException {
    	
    	try {
			Customer customer = Customer.retrieve(customerId);
			return customer.getSubscriptions().retrieve(subscriptionId);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Subscription", e);
		}
    }
    
    /**
     * Update a Subscription
     * Note that Stripe uses a specific policy around prorating changed subscriptions. If you want to use this endpoint, consider the documentation at https://stripe.com/docs/api/java#update_subscription closely.
     * 
     * @param customerId The ID of the customer to create the subscription on
     * @param subscriptionId The ID of the subscription to update
     * @param plan The identifier of the plan to update the subscription to. If omitted, the subscription will not change plans.
     * @param coupon The code of the coupon to apply to the customer if you would like to apply it at the same time as updating the subscription.
     * @param prorate Flag telling us whether to prorate switching plans during a billing cycle.
     * @param trialEnd Unix timestamp representing the end of the trial period the customer will get before being charged for the first time. If set, trial_end will override the default trial period of the plan the customer is being subscribed to. The special value now can be provided to end the customer's trial immediately.
     * @param sourceToken The source can either be a token, like the ones returned by our Stripe.js, ...
     * @param source ...or a Map containing a user's credit card details (with the options shown below). You must provide a source if the customer does not already have a valid source attached, and you are subscribing the customer for a plan that is not free. Passing source will create a new source object, make it the customer default source, and delete the old customer default if one exists. If you want to add an additional source to use with subscriptions, instead use the card creation API to add the card and then the customer update API to set it as the default. Whenever you attach a card to a customer, Stripe will automatically validate the card.
     * @param quantity The quantity you'd like to apply to the subscription you're creating. For example, if your plan is $10/user/month, and your customer has 5 users, you could pass 5 as the quantity to have the customer charged $50 (5 x $10) monthly. If you update a subscription but don't change the plan ID (e.g. changing only the trial_end), the subscription will inherit the old subscription's quantity attribute unless you pass a new quantity parameter. If you update a subscription and change the plan ID, the new subscription will not inherit the quantity attribute and will default to 1 unless you pass a quantity parameter.
     * @param applicationFeePercent A positive decimal (with at most two decimal places) between 1 and 100. This represents the percentage of the subscription invoice subtotal that will be transferred to the application owner’s Stripe account. The request must be made with an OAuth key in order to set an application fee percentage. For more information, see the application fees documentation.
     * @param taxPercent A positive decimal (with at most two decimal places) between 1 and 100. This represents the percentage of the subscription invoice subtotal that will be calculated and added as tax to the final amount each billing period. For example, a plan which charges $10/month with a tax_percent of 20.0 will charge $12 per invoice.
     * @param metadata A set of key/value pairs that you can attach to a subscription object. It can be useful for storing additional information about the subscription in a structured format.
     * @return Returns the updated Subscription
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Subscription updateSubscription(String customerId, String subscriptionId, String plan, String coupon, boolean prorate, String trialEnd, String sourceToken, Source source, int quantity, double applicationFeePercent, double taxPercent, Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	params.put("plan", plan);
		params.put("coupon", coupon);
		params.put("prorate", prorate);
		params.put("trial_end", trialEnd);
		if (sourceToken != null && !sourceToken.isEmpty()){
			params.put("source", sourceToken);
		} else if (source != null){
			params.put("source", StripeClientUtils.removeOptionals(source.toDictionary()));
		}
		params.put("quantity", quantity);
		params.put("application_fee_percent", applicationFeePercent);
		params.put("tax_percent", taxPercent);
		params.put("metadata", metadata);
		params = StripeClientUtils.removeOptionalsAndZeroes(params);
		
    	try {
			Customer customer = Customer.retrieve(customerId);
			return customer.getSubscriptions().retrieve(subscriptionId).update(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the Subscription", e);
		}
    }
    
    /**
     * Cancel a Subscription
     * 
      * @param customerId The ID of the customer to cancel the subscription of
     * @param subscriptionId ID of subscription to cancel.
     * @return Returns the deleted subscription object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Subscription cancelSubscription(String customerId, String subscriptionId)    
    		throws StripeConnectorException {
    	
    	try {
			Customer customer = Customer.retrieve(customerId);
			for(Subscription subscription : customer.getSubscriptions().getData()){
				if(subscription.getId().equals(subscriptionId)){
				    return subscription.cancel(null);				    
				}
			}
			throw new APIException("Could not find subscription to cancel", new Exception("Subscription was not found"));
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not cancel the Subscription", e);
		}
    }
    
    /**
     * List Active Subscriptions
     * 
     * @param customerId The ID of the customer whose subscriptions will be retrieved
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns a list of the customer's active subscriptions.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public CustomerSubscriptionCollection listActiveSubscriptions(String customerId, String endingBefore, int limit, String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		Customer customer = Customer.retrieve(customerId);
    		return customer.getSubscriptions().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the Subscriptions", e);
		}
    }
}
