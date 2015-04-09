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
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.ChargeRefundCollection;
import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.DeletedCard;
import com.stripe.model.DeletedStripeObject;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceLineItemCollection;
import com.stripe.model.PaymentSource;
import com.stripe.model.PaymentSourceCollection;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.stripe.model.Refund;
import com.stripe.model.Subscription;
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
    
    /**
     * Create a Charge
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-charge}
     * 
     * @param amount A positive integer in the smallest currency unit (e.g 100 cents to charge $1.00, or 1 to charge ¥1, a 0-decimal currency) representing how much to charge the card. The minimum amount is $0.50 (or equivalent in charge currency).
     * @param currency 3-letter ISO code for currency 
     * @param customerId The ID of an existing customer that will be charged in this request.
     * 					either source or customer is required
     * @param source A payment source to be charged, such as a credit card. If you also pass a customer ID, the source must be the ID of a source belonging to the customer. Otherwise, if you do not pass a customer ID, the source you provide must be a Map containing a user's credit card details. Although not all information is required, the extra info helps prevent fraud.
     * @param description An arbitrary string which you can attach to a charge object. It is displayed when in the web interface alongside the charge. Note that if you use Stripe to send automatic email receipts to your customers, your receipt emails will include the description of the charge(s) that they are describing.
     * @param metadata A set of key/value pairs that you can attach to a charge object. It can be useful for storing additional information about the customer in a structured format. It's often a good idea to store an email address in metadata for tracking later.
     * @param capture Whether or not to immediately capture the charge. When false, the charge issues an authorization (or pre-authorization), and will need to be captured later. Uncaptured charges expire in 7 days. For more information, see authorizing charges and settling later.
     * @param statementDescriptor An arbitrary string to be displayed on your customer's credit card statement. This may be up to 22 characters. As an example, if your website is RunClub and the item you're charging for is a race ticket, you may want to specify a statement_descriptor of RunClub 5K race ticket. The statement description may not include <>"' characters, and will appear on your customer's statement in capital letters. Non-ASCII characters are automatically stripped. While most banks display this information consistently, some may display it incorrectly or not at all.
     * @param receiptEmail The email address to send this charge's receipt to. The receipt will not be sent until the charge is paid. If this charge is for a customer, the email address specified here will override the customer's email address. Receipts will not be sent for test mode charges. If receipt_email is specified for a charge in live mode, a receipt will be sent regardless of your email settings.
     * @param destination An account to make the charge on behalf of. If specified, the charge will be attributed to the destination account for tax reporting, and the funds from the charge will be transferred to the destination account. The ID of the resulting transfer will be returned in the transfer field of the response. See the documentation for details.
     * @param applicationFee A fee in cents that will be applied to the charge and transferred to the application owner's Stripe account. To use an application fee, the request must be made on behalf of another account, using the Stripe-Account header, an OAuth key, or the destination parameter. For more information, see the application fees documentation.
     * @param shipping Shipping information for the charge. Helps prevent fraud on charges for physical goods.
     * @return Returns the created Charge.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Charge createCharge(int amount, String currency, @Optional String customerId, @Optional Source source, @Optional String description, @Optional Map<String, Object> metadata, @Default("true") boolean capture, @Optional String statementDescriptor, @Optional String receiptEmail, @Optional String destination, @Default("0") int applicationFee, @Optional Map<String, Object> shipping)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	if (source != null){
    		Map<String, Object> sourceDict = source.toDictionary();
    		sourceDict = removeOptionals(sourceDict);
    		params.put("source", sourceDict);    		
    	}
	    params.put("amount", amount);
		params.put("currency", currency);
		params.put("customer", customerId);
		params.put("description", description);
		params.put("metadata", metadata);
		params.put("capture", capture);
		params.put("statement_descriptor", statementDescriptor);
		params.put("receipt_email", receiptEmail);
		params.put("destination", destination);
		params.put("application_fee", applicationFee);
		params.put("shipping", shipping);
		params = removeOptionalsAndZeroes(params);
		
    	try {
			return Charge.create(params);			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the Charge", e);
		}
    }
    
    /**
     * Retrieve a Charge
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-charge}
     * 
     * @param id The identifier of the charge to be retrieved.
     * @return Returns a charge object if a valid identifier was provided, and throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Charge retrieveCharge(String id)    
    		throws StripeConnectorException {
    	try {
			return Charge.retrieve(id);			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Charge", e);
		}
    }
    
    /**
     * Update a Charge
     * Updates the specified charge by setting the values of the parameters passed. Any parameters not provided will be left unchanged.
	 * This request accepts only the description, metadata, receipt_emailand fraud_details as arguments.
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-charge}
     * 
     * @param id The id of the charge to update
     * @param description An arbitrary string which you can attach to a charge object. It is displayed when in the web interface alongside the charge. Note that if you use Stripe to send automatic email receipts to your customers, your receipt emails will include the description of the charge(s) that they are describing.
     * @param metadata A set of key/value pairs that you can attach to a charge object. It can be useful for storing additional information about the customer in a structured format. It's often a good idea to store an email address in metadata for tracking later.
     * @param receiptEmail The email address to send this charge's receipt to. The receipt will not be sent until the charge is paid. If this charge is for a customer, the email address specified here will override the customer's email address. Receipts will not be sent for test mode charges. If receipt_email is specified for a charge in live mode, a receipt will be sent regardless of your email settings.
     * @param fraudDetails A set of key/value pairs you can attach to a charge giving information about its riskiness. If you believe a charge is fraudulent, include a user_report key with a value of fraudulent. If you believe a charge is safe, include a user_report key with a value of safe. Note that you must refund a charge before setting the user_report to fraudulent. Stripe will use the information you send to improve our fraud detection algorithms.
     * @return Returns the created Charge.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Charge updateCharge(String id, @Optional String description, @Optional Map<String, Object> metadata, @Optional String receiptEmail, @Optional Map<String, Object> fraudDetails)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("description", description);
		params.put("metadata", metadata);
		params.put("receipt_email", receiptEmail);
		params.put("fraud_details", fraudDetails);
		params = removeOptionals(params);
		
    	try {
    		Charge charge = Charge.retrieve(id);
			return charge.update(params);	
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the Charge", e);
		}
    }
    
    /**
     * Capture a Charge
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:capture-charge}
     * 
     * @param id The id of the charge to capture
     * @param amount The amount to capture, which must be less than or equal to the original amount. Any additional amount will be automatically refunded.
     * @param applicationFee An application fee to add on to this charge. Can only be used with Stripe Connect. 
     * @param statementDescriptor An arbitrary string to be displayed on your customer's credit card statement. This may be up to 22 characters. As an example, if your website is RunClub and the item you're charging for is a race ticket, you may want to specify a statement_descriptor of RunClub 5K race ticket. The statement description may not include <>"' characters, and will appear on your customer's statement in capital letters. Non-ASCII characters are automatically stripped. While most banks display this information consistently, some may display it incorrectly or not at all.
     * @param receiptEmail The email the receipt should be sent to
     * @return Returns the charge object, with an updated captured property (set to true). Capturing a charge will always succeed, unless the charge is already refunded, expired, captured, or an invalid capture amount is specified, in which case this method will throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Charge captureCharge(String id, @Default("0") int amount, @Default("0") int applicationFee, @Optional String statementDescriptor, @Optional String receiptEmail)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	params.put("amount", amount);
		params.put("statement_descriptor", statementDescriptor);
		params.put("receipt_email", receiptEmail);
		params.put("application_fee", applicationFee);
		params = removeOptionalsAndZeroes(params);
		
    	try {
			Charge charge = Charge.retrieve(id);
			return charge.capture(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not capture the Charge", e);
		}
    }
    
    /**
     * List all Charges
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-charges}
     * 
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp, 
     * @param created ... or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param customer Only return charges for the customer specified by this customer ID.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return A Map with a data property that contains an array of up to limit charges, starting after charge starting_after. Each entry in the array is a separate charge object. If no more charges are available, the resulting array will be empty. If you provide a non-existent customer ID, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public ChargeCollection listAllCharges(@Optional String createdTimestamp, @Optional Map<String, String> created, @Optional String customer, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params.put("customer", customer);
    	if (createdTimestamp != null && !createdTimestamp.isEmpty()){
    		params.put("created", createdTimestamp);
    	} else {
    		params.put("created", created);
    	}
    	
    	params = removeOptionalsAndZeroes(params);
    	try {
    		return Charge.all(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the charges", e);
		}
    }
    
    /**
     * Create a Subscription
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-subscription}
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
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Subscription createSubscription(String customerId, String plan, @Optional String coupon, @Optional String trialEnd, @Optional String sourceToken, @Optional Source source, @Default("1") int quantity, @Default("0") double applicationFeePercent, @Default("0") double taxPercent, @Optional Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	params.put("plan", plan);
		params.put("coupon", coupon);
		params.put("trial_end", trialEnd);
		if (sourceToken != null && !sourceToken.isEmpty()){
			params.put("source", sourceToken);
		} else {
			params.put("source", removeOptionals(source.toDictionary()));
		}
		params.put("quantity", quantity);
		params.put("application_fee_percent", applicationFeePercent);
		params.put("tax_percent", taxPercent);
		params.put("metadata", metadata);
		params = removeOptionalsAndZeroes(params);
		
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
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-subscription}
     * 
     * @param customerId The ID of the customer to create the subscription on
     * @param subscriptionId ID of subscription to retrieve.
     * @return Returns the subscription object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
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
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-subscription}
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
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Subscription updateSubscription(String customerId, String subscriptionId, String plan, @Optional String coupon, @Default("true") boolean prorate, @Optional String trialEnd, @Optional String sourceToken, @Optional Source source, @Default("1") int quantity, @Default("0") double applicationFeePercent, @Default("0") double taxPercent, @Optional Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	params.put("plan", plan);
		params.put("coupon", coupon);
		params.put("prorate", prorate);
		params.put("trial_end", trialEnd);
		if (sourceToken != null && !sourceToken.isEmpty()){
			params.put("source", sourceToken);
		} else {
			params.put("source", removeOptionals(source.toDictionary()));
		}
		params.put("quantity", quantity);
		params.put("application_fee_percent", applicationFeePercent);
		params.put("tax_percent", taxPercent);
		params.put("metadata", metadata);
		params = removeOptionalsAndZeroes(params);
		
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
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:cancel-subscription}
     * 
     * @param customerId The ID of the customer to cancel the subscription of
     * @param subscriptionId ID of subscription to cancel.
     * @return Returns the deleted subscription object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
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
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-active-subscriptions}
     * 
     * @param customerId The ID of the customer whose subscriptions will be retrieved
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns a list of the customer's active subscriptions.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public CustomerSubscriptionCollection listActiveSubscriptions(String customerId, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params = removeOptionalsAndZeroes(params);
    	try {
    		Customer customer = Customer.retrieve(customerId);
    		return customer.getSubscriptions().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the Subscriptions", e);
		}
    }
    
    /**
     * Create a Refund
     * When you create a new refund, you must specify a charge to create it on.
	 * Creating a new refund will refund a charge that has previously been created but not yet refunded. Funds will be refunded to the credit or debit card that was originally charged. The fees you were originally charged are also refunded.
	 * You can optionally refund only part of a charge. You can do so as many times as you wish until the entire charge has been refunded.
	 * Once entirely refunded, a charge can't be refunded again. This method will throw an error when called on an already-refunded charge, or when trying to refund more money than is left on a charge.
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-refund}
     * 
     * @param id The identifier of the charge to be refunded.
     * @param amount A positive integer in cents representing how much of this charge to refund. Can only refund up to the unrefunded amount remaining of the charge.
     * @param refundApplicationFee Boolean indicating whether the application fee should be refunded when refunding this charge. If a full charge refund is given, the full application fee will be refunded. Else, the application fee will be refunded with an amount proportional to the amount of the charge refunded. An application fee can only be refunded by the application that created the charge.
     * @param reason String indicating the reason for the refund. If set, possible values are duplicate, fraudulent, and requested_by_customer. Specifying fraudulent as the reason when you believe the charge to be fraudulent will help us improve our fraud detection algorithms.
     * @param metadata A set of key/value pairs that you can attach to a refund object. It can be useful for storing additional information about the refund in a structured format. You can unset an individual key by setting its value to null and then saving. To clear all keys, set metadata to null, then save.
     * @return Returns the refund object if the refund succeeded. Throws an error if the charge has already been refunded or an invalid charge identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Refund createRefund(String id, @Default("0") int amount, @Default("false") boolean refundApplicationFee, @Optional String reason, @Optional Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
    	params.put("refund_application_fee", refundApplicationFee);
    	params.put("reason", reason);
    	params.put("metadata", metadata);
    	params = removeOptionalsAndZeroes(params);
    	try {
    		Charge charge = Charge.retrieve(id);
    		return charge.getRefunds().create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the Refund", e);
		}
    }
    
    /**
     * Retrieve a Refund
     * By default, you can see the 10 most recent refunds stored directly on the charge object, but you can also retrieve details about a specific refund stored on the charge.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-refund}
     * 
     * @param id The identifier of the refund
     * @param chargeId The identifier of the Charge refunded.
     * @return Returns Returns the refund object if found.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Refund retrieveRefund(String id, String chargeId)    
    		throws StripeConnectorException {
    	try {
    		Charge charge = Charge.retrieve(chargeId);
    		return charge.getRefunds().retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Refund", e);
		}
    }
    
    /**
     * Update a Refund
     * Updates the specified refund by setting the values of the parameters passed. Any parameters not provided will be left unchanged.
	 * This request only accepts metadata as an argument.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-refund}
     * 
     * @param id The identifier of the refund
     * @param chargeId The identifier of the Charge refunded.
     * @param metadata A set of key/value pairs that you can attach to a refund object. It can be useful for storing additional information about the refund in a structured format.
     * @return Returns the updated refund.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Refund updateRefund(String id, String chargeId, Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	try {
    		Map<String, Object> params = new HashMap<String, Object>();
    		params.put("metadata", metadata);
    		Charge charge = Charge.retrieve(chargeId);
    		Refund refund = charge.getRefunds().retrieve(id);
    		return refund.update(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the Refund", e);
		}
    }
    
    /**
     * List All Refunds
     * You can see a list of the refunds belonging to a specific charge. Note that the 10 most recent refunds are always available by default on the charge object. If you need more than those 10, you can use this API method and the limit and starting_after parameters to page through additional refunds.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-refunds}
     * 
     * @param chargeId The ID of the charge whose refunds will be retrieved.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns a list of the charge's refunds
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public ChargeRefundCollection listAllRefunds(String chargeId, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params = removeOptionalsAndZeroes(params);
    	try {
    		Charge charge = Charge.retrieve(chargeId);
    		return charge.getRefunds().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the Refunds", e);
		}
    }
    
    /**
     * Create an invoice
	 * Invoices are statements of what a customer owes for a particular billing period, including subscriptions, invoice items, and any automatic proration adjustments if necessary.
	 * Once an invoice is created, payment is automatically attempted. Note that the payment, while automatic, does not happen exactly at the time of invoice creation. If you have configured webhooks, the invoice will wait until one hour after the last webhook is successfully sent (or the last webhook times out after failing).
	 * Any customer credit on the account is applied before determining how much is due for that invoice (the amount that will be actually charged). If the amount due for the invoice is less than 50 cents (the minimum for a charge), we add the amount to the customer's running account balance to be added to the next invoice. If this amount is negative, it will act as a credit to offset the next invoice. Note that the customer account balance does not include unpaid invoices; it only includes balances that need to be taken into account when calculating the amount due for the next invoice.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-invoice}
     * 
     * @param customerId The customer to create the invoice for
     * @param applicationFee A fee in cents that will be applied to the invoice and transferred to the application owner’s Stripe account.
     * @param description
     * @param metadata
     * @param statementDescriptor Extra information about a charge for the customer’s credit card statement.
     * @param subscription The ID of the subscription to invoice. If not set, the created invoice will include all pending invoice items for the customer. If set, the created invoice will exclude pending invoice items that pertain to other subscriptions.
     * @param taxPercent The percent tax rate applied to the invoice, represented as a decimal number.
     * @return Returns the invoice object if there are pending invoice items to invoice. Throws an error if there are no pending invoice items or if the customer ID provided is invalid.    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Invoice createInvoice(String customerId, @Default("0") int applicationFee, @Optional String description, @Optional Map<String, Object> metadata, @Optional String statementDescriptor, @Optional String subscription, @Default("0.0") double taxPercent)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customerId);
    	params.put("application_fee", applicationFee);
    	params.put("description", description);
    	params.put("metadata", metadata);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("subscription", subscription);
    	params.put("tax_percent", taxPercent);
    	params = removeOptionalsAndZeroes(params);
    	try {    		
    		return Invoice.create(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the Invoice", e);
		}
    }
    
    /**
     * Retrieve an Invoice
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-invoice}
     * 
     * @param id The identifier of the desired invoice
     * @return Returns the invoice requested.    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Invoice retrieveInvoice(String id)    
    		throws StripeConnectorException {
    	try {    		
    		return Invoice.retrieve(id);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Invoice", e);
		}
    }
    
    /**
     * Retrieve an invoice's line items
	 * When retrieving an invoice, you'll get a lines property containing the total count of line items and the first handful of those items. There is also a URL where you can retrieve the full (paginated) list of line items.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-invoice-line-items}
     * 
     * @param id The id of the invoice containing the lines to be retrieved
     * @param customer In the case of upcoming invoices, the customer of the upcoming invoice is required. In other cases it is ignored.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @param subscription In the case of upcoming invoices, the subscription of the upcoming invoice is optional. In other cases it is ignored.
     * @return Returns the collection of invoice line items    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public InvoiceLineItemCollection retrieveInvoiceLineItems(String id, @Optional String customer, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter, @Optional String subscription)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customer);
    	params.put("endingBefore", endingBefore);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params.put("subscription", subscription);
    	params = removeOptionalsAndZeroes(params);
    	try {    		
    		Invoice invoice = Invoice.retrieve(id);
    		return invoice.getLines().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Invoice Line Items", e);
		}
    }
    
    /**
     * Retrieve an upcoming invoice
	 * At any time, you can preview the upcoming invoice for a customer. This will show you all the charges that are pending, including subscription renewal charges, invoice item charges, etc. It will also show you any discount that is applicable to the customer.
	 * Note that when you are viewing an upcoming invoice, you are simply viewing a preview -- the invoice has not yet been created. As such, the upcoming invoice will not show up in invoice listing calls, and you cannot use the API to pay or edit the invoice. If you want to change the amount that your customer will be billed, you can add, remove, or update pending invoice items, or update the customer's discount.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-upcoming-invoice}
     * 
     * @param customerId The identifier of the customer whose upcoming invoice you'd like to retrieve.
     * @param subscription The identifier of the subscription for which you'd like to retrieve the upcoming invoice. If not provided, you will retrieve the next upcoming invoice from among the customer's subscriptions.
     * @return Returns an invoice if a valid customer ID was provided. Throws an error otherwise.    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Invoice retrieveUpcomingInvoice(String customerId, @Optional String subscription)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customerId);
    	params.put("subscription", subscription);
    	params = removeOptionalsAndZeroes(params);
    	try {    		
    		return Invoice.upcoming(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the upcoming Invoice", e);
		}
    }
    
    /**
     * Update an invoice
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-invoice}
     * 
     * @param invoiceId The invoice to update
     * @param applicationFee A fee in cents that will be applied to the invoice and transferred to the application owner’s Stripe account.
     * @param closed Boolean representing whether an invoice is closed or not. To close an invoice, pass true.
     * @param description
     * @param forgiven Boolean representing whether an invoice is forgiven or not. To forgive an invoice, pass true. Forgiving an invoice instructs us to update the subscription status as if the invoice were succcessfully paid. Once an invoice has been forgiven, it cannot be unforgiven or reopened.    
     * @param metadata
     * @param statementDescriptor Extra information about a charge for the customer’s credit card statement.
     * @param taxPercent The percent tax rate applied to the invoice, represented as a decimal number.
     * @return Returns the invoice object     
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Invoice updateInvoice(String invoiceId, @Default("0") int applicationFee, @Default("false") boolean closed, @Optional String description, @Default("false") boolean forgiven, @Optional Map<String, Object> metadata, @Optional String statementDescriptor, @Default("0.0") double taxPercent)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("application_fee", applicationFee);
    	params.put("closed", closed);
    	params.put("description", description);
    	params.put("forgiven", forgiven);
    	params.put("metadata", metadata);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("tax_percent", taxPercent);
    	params = removeOptionalsAndZeroes(params);
    	try {    		
    		Invoice invoice = Invoice.retrieve(invoiceId);
    		return invoice.update(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the Invoice", e);
		}
    }
    
    /**
     * Pay an invoice
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:pay-invoice}
     * 
     * @param id the invoice id
     * @return Returns the invoice object     
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Invoice payInvoice(String id)    
    		throws StripeConnectorException {
    	try {    		
    		Invoice invoice = Invoice.retrieve(id);
    		return invoice.pay();
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not pay the Invoice", e);
		}
    }
    
    /**
     * List all Invoices
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-all-invoices}
     * 
     * @param customerId The id of the customer
     * @param dateTimestamp A filter on the list based on the object date field. The value can be a string with an integer Unix timestamp,...
     * @param date or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns the collection of invoices
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public InvoiceCollection retrieveAllInvoices(@Optional String customerId, @Optional String dateTimestamp, @Optional Map<String, Object> date, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customerId);
    	if (dateTimestamp != null && !dateTimestamp.isEmpty()){
    		params.put("date", dateTimestamp);
    	} else {
    		params.put("date", date);
    	}
    	params.put("endingBefore", endingBefore);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params = removeOptionalsAndZeroes(params);
    	try {    		
    		return Invoice.all(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve Invoices", e);
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
            if (pair.getValue() == null || pair.getValue().toString().equals("") || pair.getValue().toString().equals("0") || pair.getValue().toString().equals("0.0")){
            	it.remove();
            }             
        }
        return map;
    }
    
}