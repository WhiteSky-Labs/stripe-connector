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
import com.stripe.model.Balance;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import com.stripe.model.Card;
import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.DeletedCard;
import com.stripe.model.PaymentSource;
import com.stripe.model.PaymentSourceCollection;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.wsl.modules.stripe.complextypes.Source;
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
		customerParams.put("coupon", couponCode);
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
    public Customer updateCustomer(String id, @Default("0") final int accountBalance, @Optional final String couponCode, @Optional final String description, @Optional final String email, @Optional final Map<String, Object> metadata, @Optional final String sourceToken) //NOSONAR 
    		throws StripeConnectorException {
    	Map<String, Object> customerParams = new HashMap<String, Object>();
    	if (accountBalance > 0) {
    		customerParams.put("account_balance", accountBalance);
    	}
		customerParams.put("coupon", couponCode);
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
     * @param startingAfter A cursor (id) for use in pagination.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list.
     * @return CustomerCollection of Customers found
     * @throws StripeConnectorException when there is an issue listing customers         
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public CustomerCollection listAllCustomers(@Default("0") int limit, @Optional String startingAfter, @Optional String endingBefore) throws StripeConnectorException{
    	Map<String, Object> customerParams = new HashMap<String, Object>();
    	if (limit > 0){
    		customerParams.put("limit", limit);
    	}
    	customerParams.put("starting_after", startingAfter);
    	customerParams.put("ending_before", endingBefore);
    	customerParams = removeOptionals(customerParams);
    	try {
			return Customer.all(customerParams);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a customer", e);
		}
    }

    /**
     * Create a Plan
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-plan}
     *
     * @param id Unique string of your choice that will be used to identify this plan when subscribing a customer. This could be an identifier like "gold" or a primary key from your own database.
     * @param amount A positive integer in cents (or 0 for a free plan) representing how much to charge (on a recurring basis).
     * @param currency 3-letter ISO code for currency.
     * @param interval Specifies billing frequency. Either day, week, month or year.
     * @param intervalCount The number of intervals between each subscription billing. For example, interval=month and interval_count=3 bills every 3 months. Maximum of one year interval allowed (1 year, 12 months, or 52 weeks).
     * @param planName Name of the plan, to be displayed on invoices and in the web interface.
     * @param trialPeriodDays Specifies a trial period in (an integer number of) days. If you include a trial period, the customer won't be billed for the first time until the trial period ends. If the customer cancels before the trial period is over, she'll never be billed at all.
     * @param statementDescriptor An arbitrary string to be displayed on your customer's credit card statement. This may be up to22 characters. 
     * @param metadata A set of key/value pairs that you can attach to a plan object. It can be useful for storing additional information about the plan in a structured format.
     * @return Plan the created Plan
     * @throws StripeConnectorException when there is an issue creating a Plan 
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Plan createPlan(String id, int amount, String currency, String interval, @Default("1") int intervalCount, String planName, @Default("0") int trialPeriodDays, @Optional String statementDescriptor, @Optional Map<String, Object> metadata) throws StripeConnectorException{
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (trialPeriodDays > 0){
    		params.put("trial_period_days", trialPeriodDays);
    	}
    	params.put("id", id);
    	params.put("amount", amount);
    	params.put("currency", currency);
    	params.put("interval", interval);
    	params.put("interval_count", intervalCount);
    	params.put("name", planName);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("metadata", metadata);
    	params = removeOptionals(params);
    	try {
			return Plan.create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a plan", e);
		}
    }
    
    /**
     * Retrieve a Plan
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-plan}
     *
     * @param id The id of the plan to retrieve
     * @return Plan the retrieved Plan
     * @throws StripeConnectorException when there is an issue retrieving a Plan 
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Plan retrievePlan(String id) throws StripeConnectorException{
    	try {
			return Plan.retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the plan", e);
		}
    }
    
    /**
     * Update a Plan
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-plan}
     *
     * @param id of the plan to update
     * @param planName Name of the plan, to be displayed on invoices and in the web interface.
     * @param statementDescriptor An arbitrary string to be displayed on your customer's credit card statement. This may be up to22 characters. 
     * @param metadata A set of key/value pairs that you can attach to a plan object. It can be useful for storing additional information about the plan in a structured format.
     * @return Plan the updated Plan
     * @throws StripeConnectorException when there is an issue updating a Plan 
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Plan updatePlan(String id, @Optional String planName, @Optional String statementDescriptor, @Optional Map<String, Object> metadata) throws StripeConnectorException{
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("name", planName);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("metadata", metadata);
    	params = removeOptionals(params);
    	try {
			Plan plan = Plan.retrieve(id);
			return plan.update(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a plan", e);
		}
    }
    
    /**
     * Delete a Plan
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:delete-plan}
     *
     * @param id The id of the plan to delete
     * @return Object JSON containing success or failure
     * @throws StripeConnectorException when there is an issue deleting a Plan 
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Object deletePlan(String id) throws StripeConnectorException{
    	try {
    		Plan plan = Plan.retrieve(id);
			return plan.delete();
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the plan", e);
		}
    }
    
    /**
     * List All Plans
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-plan}
     *
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp.
     * @param created A filter on the list based on the object created field. The value can be a dictionary containing gt, gte, lt and/or lte values. You cannot supply a created value and this dictionary at the same time.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return PlanCollection the plans that matched the criteria
     * @throws StripeConnectorException when there is an issue listing plans
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public PlanCollection listAllPlans(@Optional String createdTimestamp, @Optional Map<String, String> created, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter) throws StripeConnectorException{
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (limit > 0){
    		params.put("limit", limit);
    	}
    	if (createdTimestamp != null){
    		params.put("created", createdTimestamp);
    	} else {
    		params.put("created", created);
    	}
    	params.put("ending_before", endingBefore);
    	params.put("starting_after", startingAfter);
    	params = removeOptionals(params);
    	try {
    		return Plan.all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list plans", e);
		}
    }
    
    /**
     * Create a Coupon
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-coupon}
     *
     * @param id Unique string of your choice that will be used to identify this coupon when applying it a customer. This is often a specific code you’ll give to your customer to use when signing up (e.g. FALL25OFF). If you don’t want to specify a particular code, you can leave the ID blank and we’ll generate a random code for you.
     * @param duration Specifies how long the discount will be in effect. Can be forever, once, or repeating.
     * @param amountOff A positive integer representing the amount to subtract from an invoice total (required if percent_off is not passed)
     * @param currency Currency of the amount_off parameter (required if amount_off is passed)
     * @param durationInMonths required only if duration is repeating If duration is repeating, a positive integer that specifies the number of months the discount will be in effect
     * @param maxRedemptions A positive integer specifying the number of times the coupon can be redeemed before it’s no longer valid. For example, you might have a 50% off coupon that the first 20 readers of your blog can use.
     * @param percentOff A positive integer between 1 and 100 that represents the discount the coupon will apply (required if amount_off is not passed)
     * @param redeemBy Unix timestamp specifying the last time at which the coupon can be redeemed. After the redeem_by date, the coupon can no longer be applied to new customers.
     * @param metadata A set of key/value pairs that you can attach to a coupon object. It can be useful for storing additional information about the coupon in a structured format. 
     * @return Coupon created
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Coupon createCoupon(@Optional String id, String duration, @Default("0") int amountOff, @Optional String currency, @Default("0") int durationInMonths, @Default("0") int maxRedemptions, @Default("0") int percentOff, @Optional String redeemBy, @Optional Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", id);
    	params.put("duration", duration);
    	params.put("amount_off", amountOff);
    	params.put("currency", currency);
    	params.put("duration_in_months", durationInMonths);
    	params.put("max_redemptions", maxRedemptions);
    	params.put("metadata", metadata);
    	params.put("percent_off", percentOff);
    	params.put("redeem_by", redeemBy);
		params = removeOptionalsAndZeroes(params);
    	try {
			return Coupon.create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the coupon", e);
		}
    }
    
    /**
     * Retrieve a Coupon
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-coupon}
     *
     * @param id The ID of the desired coupon.
     * @return Coupon retrieved
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Coupon retrieveCoupon(String id) 
    		throws StripeConnectorException {
    	try {
			return Coupon.retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the coupon", e);
		}
    }
    
    /**
     * Update a Coupon
     * Updates the metadata of a coupon. Other coupon details (currency, duration, amount_off) are, by design, not editable.
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-coupon}
     *
     * @param id The ID of the coupon to be updated.
     * @param metadata A set of key/value pairs that you can attach to a coupon object. It can be useful for storing additional information about the coupon in a structured format.
     * @return Coupon updated
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Coupon updateCoupon(String id, @Optional Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("metadata", metadata);
    	try {
    		Coupon coupon = Coupon.retrieve(id);
			return coupon.update(params);			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the coupon", e);
		}
    }
    
    /**
     * Delete a Coupon
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:delete-coupon}
     *
     * @param id The ID of the coupon to be deleted.     
     * @return An object with the deleted coupon's ID and a deleted flag upon success. Otherwise, this call throws an error, such as if the coupon has already been deleted.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Object deleteCoupon(String id) 
    		throws StripeConnectorException {
    	try {
    		Coupon coupon = Coupon.retrieve(id);
			return coupon.delete();			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not delete the coupon", e);
		}
    }
    
    /**
     * List all Coupons
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-coupons}
     *
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp,...
     * @param created ... or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.     
     * @return A Map with a data property that contains an array of up to limit coupons, starting after coupon starting_after. Each entry in the array is a separate coupon object. If no more coupons are available, the resulting array will be empty. This request should never throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public CouponCollection listAllCoupons(@Optional String createdTimestamp, @Optional Map<String, String> created, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter) 
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (limit != 0){
    		params.put("limit", limit);
    	}
    	if (createdTimestamp != null && !createdTimestamp.isEmpty()){
    		params.put("created", createdTimestamp);
    	} else {
    		params.put("created", created);
    	}
    	params.put("ending_before", endingBefore);
    	params.put("starting_after", startingAfter);
    	params = removeOptionals(params);
    	try {
    		return Coupon.all(params);			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list coupons", e);
		}
    }
    
    /**
     * Retrieve the balance
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-balance}
     *
     * @return Returns a balance object for the API key used.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Balance retrieveBalance()    
    		throws StripeConnectorException {
    	try {
    		return Balance.retrieve();			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the balance", e);
		}
    }
    
    /**
     * Retrieve a Balance Transaction
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-balance-transaction}
     *
     * @param id The id of the transaction
     * @return Returns a balance transaction if a valid balance transaction ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public BalanceTransaction retrieveBalanceTransaction(String id) 
    		throws StripeConnectorException {
    	try {
    		return BalanceTransaction.retrieve(id);			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Balance Transaction", e);
		}
    }
    
    /**
     * List all Balance History
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-balance-history}
     * 
     * @param availableOnTimestamp A filter on the list based on the object available_on field. The value can be a string with an integer Unix timestamp,
     * @param availableOn ... or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp,
     * @param created ... or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param currency The Currency Code
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param sourceId Only returns transactions that are related to the specified Stripe object ID (e.g. filtering by a charge ID will return all charge and refund transactions).
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @param transfer For automatic Stripe transfers only, only returns transactions that were transferred out on the specified transfer ID.
     * @param type Only returns transactions of the given type. One of: charge, refund, adjustment, application_fee, application_fee_refund, transfer, or transfer_failure 
     * @return Returns a balance object for the API key used.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public BalanceTransactionCollection listAllBalanceHistory(@Optional String availableOnTimestamp, @Optional Map<String, String> availableOn, @Optional String createdTimestamp, @Optional Map<String, String> created, @Optional String currency, @Optional String endingBefore, @Default("0") int limit, @Optional String sourceId, @Optional String startingAfter, @Optional String transfer, @Optional String type)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	if (availableOnTimestamp != null && !availableOnTimestamp.isEmpty()){
    		params.put("available_on", availableOnTimestamp);
    	} else {
    		params.put("available_on", availableOn);    		
    	}
    	if (createdTimestamp != null && !createdTimestamp.isEmpty()){
    		params.put("created", createdTimestamp);
    	} else {
    		params.put("created", created);    		
    	}
    	params.put("currency", currency);
    	params.put("ending_before", endingBefore);
    	params.put("source", sourceId);
    	params.put("startingAfter", startingAfter);
    	params.put("transfer", transfer);
    	params.put("type", type);    	
    	params = removeOptionalsAndZeroes(params);
    	try {
    		return BalanceTransaction.all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the balance history", e);
		}
    }
    
    /**
     * Create a Card
     * Note that this is only available for Customers at this time, as Recipients (of Transfers) have been deprecated by Stripe.
     * Stripe Connect is the recommended means for transferring funds.
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-card}
     * 
     * @param ownerId the ID for the Customer or Recipient
     * @param sourceToken The source can either be a token, like the ones returned by Stripe.js, 
     * @param source or a dictionary containing a user’s credit card details (with the options shown below). Whenever you create a new card for a customer, Stripe will automatically validate the card.
     * @return Returns the created Card.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Card createCard(String ownerId, @Optional String sourceToken, @Optional Source source)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	if (sourceToken != null && !sourceToken.isEmpty()){
    		params.put("source", sourceToken);
    	} else {
    		Map<String, Object> sourceDict = source.toDictionary();
    		sourceDict = removeOptionals(sourceDict);
    		params.put("source", sourceDict);    		
    	}
    	try {
			Customer customer = Customer.retrieve(ownerId);
			return customer.createCard(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the Card", e);
		}
    }
    
    /**
     * Retrieve a Card
     * Note that this is only available for Customers at this time, as Recipients (of Transfers) have been deprecated by Stripe.
     * Stripe Connect is the recommended means for transferring funds.
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-card}
     * 
     * @param ownerId The ID of the owner of the card.
     * @param id The ID of the card to be retrieved.
     * @return Returns the retrieved Card.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Card retrieveCard(String ownerId, String id)    
    		throws StripeConnectorException {
    	try {
			Customer customer = Customer.retrieve(ownerId);
			PaymentSource source = customer.getSources().retrieve(id);
			if (source.getObject().equals("card")){
				return (Card) source; 
			} else {
				throw new CardException("The source was not a card", "001", id, new Exception("Source was not a card type"));
			}		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Card", e);
		}
    }
    
    /**
     * Update a Card
     * Note that this is only available for Customers at this time, as Recipients (of Transfers) have been deprecated by Stripe.
     * Stripe Connect is the recommended means for transferring funds.
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-card}
     * 
     * @param ownerId The ID of the owner of the card.
     * @param id The ID of the card to be updated.
     * @param addressCity The City component of the Card Address
     * @param addressCountry The Country component of the Card Address
     * @param addressLine1 The Address, Line 1, component of the Card Address
     * @param addressLine2 The Address, Line 2, component of the Card Address
     * @param addressState The State component of the Card Address
     * @param addressZip The Zipcode component of the Card Address
     * @param expMonth The Card's expiry month
     * @param expYear The Card's expiry year
     * @param metadata Arbitrary metadata to attach to the card
     * @param cardName The name for the card
     * 
     * @return Returns the updated Card.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Card updateCard(String ownerId, String id, @Optional String addressCity, @Optional String addressCountry, @Optional String addressLine1, @Optional String addressLine2, @Optional String addressState, @Optional String addressZip, @Optional String expMonth, @Optional String expYear, @Optional Map<String, Object> metadata, @Optional String cardName)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("address_city", addressCity);
    	params.put("address_country", addressCountry);
    	params.put("address_line1", addressLine1);
    	params.put("address_line2", addressLine2);
    	params.put("address_state", addressState);
    	params.put("address_zip", addressZip);
    	params.put("exp_month", expMonth);
    	params.put("exp_year", expYear);
    	params.put("metadata", metadata);
    	params.put("name", cardName);
    	params = removeOptionals(params);
    	try {
			Customer customer = Customer.retrieve(ownerId);
			PaymentSource source = customer.getSources().retrieve(id);
			return (Card) source.update(params);		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the Card", e);
		}
    }
    
    /**
     * Delete a Card
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:delete-card}
     * 
     * @param ownerId The ID of the owner of the card.
     * @param id The ID of the card to be deleted.
     * @return Returns the deleted card object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public DeletedCard deleteCard(String ownerId, String id)    
    		throws StripeConnectorException {
    	try {
			Customer customer = Customer.retrieve(ownerId);
			for(PaymentSource source : customer.getSources().getData()){
			  if(source.getId().equals(id)){
			    return (DeletedCard) source.delete();
			  }
			}
			// if we get to here, the card wasn't found - throw an exception
    		throw new CardException("The card wasn't found", "001", id, new Exception("Card id was not found"));
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not delete the Card", e);
		}
    }
    
    /**
     * List all Customer Cards
     * Note that this is only available for Customers at this time, as Recipients (of Transfers) have been deprecated by Stripe.
     * Stripe Connect is the recommended means for transferring funds.
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-customer-cards}
     * 
     * @param ownerId The ID of the customer whose cards will be retrieved
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns a list of the cards stored on the customer or recipient.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public PaymentSourceCollection listAllCustomerCards(String ownerId, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params = removeOptionalsAndZeroes(params);
    	try {
    		Customer customer = Customer.retrieve(ownerId);
    		return customer.getSources().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the customer cards", e);
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
            Map.Entry pair = it.next();
            if (pair.getValue() == null || pair.getValue().toString().equals("")){
            	it.remove();
            }             
        }
        return map;
    }
    
    private Map<String, Object> removeOptionalsAndZeroes(Map<String, Object> map){
    	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = it.next();
            if (pair.getValue() == null || pair.getValue().toString().equals("") || pair.getValue().toString().equals("0")){
            	it.remove();
            }             
        }
        return map;
    }

}