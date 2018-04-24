/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe;

import java.util.Map;

import org.mule.api.MuleContext;
import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.TransformerResolver;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParamAffectsType;
import org.mule.api.annotations.param.Optional;
import org.mule.api.transformer.DataType;
import org.mule.transport.NullPayload;

import com.stripe.model.Account;
import com.stripe.model.ApplicationFee;
import com.stripe.model.ApplicationFeeCollection;
import com.stripe.model.Balance;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import com.stripe.model.BitcoinReceiver;
import com.stripe.model.BitcoinReceiverCollection;
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
import com.stripe.model.Event;
import com.stripe.model.EventCollection;
import com.stripe.model.FeeRefund;
import com.stripe.model.FeeRefundCollection;
import com.stripe.model.FileUpload;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceLineItemCollection;
import com.stripe.model.PaymentSourceCollection;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.stripe.model.Refund;
import com.stripe.model.Subscription;
import com.stripe.model.Token;
import com.wsl.modules.stripe.client.StripeAccountClient;
import com.wsl.modules.stripe.client.StripeApplicationFeeClient;
import com.wsl.modules.stripe.client.StripeBalanceClient;
import com.wsl.modules.stripe.client.StripeBitcoinReceiverClient;
import com.wsl.modules.stripe.client.StripeCardClient;
import com.wsl.modules.stripe.client.StripeChargeClient;
import com.wsl.modules.stripe.client.StripeCouponClient;
import com.wsl.modules.stripe.client.StripeCustomerClient;
import com.wsl.modules.stripe.client.StripeEventClient;
import com.wsl.modules.stripe.client.StripeFileUploadClient;
import com.wsl.modules.stripe.client.StripeInvoiceClient;
import com.wsl.modules.stripe.client.StripePlanClient;
import com.wsl.modules.stripe.client.StripeRefundClient;
import com.wsl.modules.stripe.client.StripeSubscriptionClient;
import com.wsl.modules.stripe.client.StripeTokenClient;
import com.wsl.modules.stripe.complextypes.Acceptance;
import com.wsl.modules.stripe.complextypes.BankAccount;
import com.wsl.modules.stripe.complextypes.CreateAccountParameters;
import com.wsl.modules.stripe.complextypes.CreateChargeParameters;
import com.wsl.modules.stripe.complextypes.CreateSubscriptionParameters;
import com.wsl.modules.stripe.complextypes.FilePurpose;
import com.wsl.modules.stripe.complextypes.LegalEntity;
import com.wsl.modules.stripe.complextypes.ListAllBalanceHistoryParameters;
import com.wsl.modules.stripe.complextypes.ListAllCouponsParameters;
import com.wsl.modules.stripe.complextypes.ListAllPlansParameters;
import com.wsl.modules.stripe.complextypes.Source;
import com.wsl.modules.stripe.complextypes.TimeRange;
import com.wsl.modules.stripe.complextypes.TransferSchedule;
import com.wsl.modules.stripe.complextypes.UpdateAccountParameters;
import com.wsl.modules.stripe.complextypes.UpdateSubscriptionParameters;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.strategy.ConnectorConnectionStrategy;
import com.wsl.modules.stripe.utils.NullPayloadToEmptyMapTransformer;

/**
 * Anypoint Connector
 *
 * @author WhiteSky Labs
 */
@Connector(name="stripe", friendlyName="Stripe", minMuleVersion="3.6")
public class StripeConnector {
	
    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;
    
    StripeCustomerClient customerClient = new StripeCustomerClient();
    StripePlanClient planClient = new StripePlanClient();
    StripeCouponClient couponClient = new StripeCouponClient();
    StripeBalanceClient balanceClient = new StripeBalanceClient();
    StripeCardClient cardClient = new StripeCardClient();
    StripeChargeClient chargeClient = new StripeChargeClient();
    StripeSubscriptionClient subClient = new StripeSubscriptionClient();
    StripeRefundClient refundClient = new StripeRefundClient();
    StripeInvoiceClient invoiceClient = new StripeInvoiceClient();
    StripeApplicationFeeClient feeClient = new StripeApplicationFeeClient();
    StripeAccountClient accountClient = new StripeAccountClient();
    StripeTokenClient tokenClient = new StripeTokenClient();
    StripeEventClient eventClient = new StripeEventClient();
    StripeBitcoinReceiverClient bitcoinClient = new StripeBitcoinReceiverClient();
    StripeFileUploadClient fileClient = new StripeFileUploadClient();
    
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Customer createCustomer(@Default("0") int accountBalance, @Optional String couponCode, @Optional String description, @Optional String email, @Default("#[payload]") Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	return customerClient.createCustomer(accountBalance, couponCode, description, email, metadata);    	
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Customer retrieveCustomer(String id) 
    		throws StripeConnectorException {
    	return customerClient.retrieveCustomer(id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Customer updateCustomer(String id, @Default("0") int accountBalance, @Optional String couponCode, @Optional String description, @Optional String email, @Default("#[payload]") Map<String, Object> metadata, @Optional String sourceToken)  
    		throws StripeConnectorException {
    	return customerClient.updateCustomer(id, accountBalance, couponCode, description, email, metadata, sourceToken);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Object deleteCustomer(String id) 
    		throws StripeConnectorException {
    	return customerClient.deleteCustomer(id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public CustomerCollection listAllCustomers(@Default("0") int limit, @Optional String startingAfter, @Optional String endingBefore) throws StripeConnectorException{
    	return customerClient.listAllCustomers(limit, startingAfter, endingBefore);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Plan createPlan(String id, int amount, String currency, String interval, @Default("1") int intervalCount, String planName, @Default("0") int trialPeriodDays, @Optional String statementDescriptor, @Default("#[payload]") Map<String, Object> metadata) throws StripeConnectorException{
    	return planClient.createPlan(id, amount, currency, interval, intervalCount, planName, trialPeriodDays, statementDescriptor, metadata);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Plan retrievePlan(String id) throws StripeConnectorException{
    	return planClient.retrievePlan(id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Plan updatePlan(String id, @Optional String planName, @Optional String statementDescriptor, @Default("#[payload]") Map<String, Object> metadata) throws StripeConnectorException{
    	return planClient.updatePlan(id, planName, statementDescriptor, metadata);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Object deletePlan(String id) throws StripeConnectorException{
    	return planClient.deletePlan(id);
    }
    
    /**
     * List All Plans
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-plans}
     *
     * @param listAllPlansParameters An object that wraps the parameters for List All Plans
     * @return PlanCollection the plans that matched the criteria
     * @throws StripeConnectorException when there is an issue listing plans
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public PlanCollection listAllPlans(@Default("#[payload]") ListAllPlansParameters listAllPlansParameters) throws StripeConnectorException{
    	return planClient.listAllPlans(listAllPlansParameters.getCreatedTimestamp(), listAllPlansParameters.getCreated(), listAllPlansParameters.getEndingBefore(), listAllPlansParameters.getLimit(), listAllPlansParameters.getStartingAfter());
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Coupon createCoupon(@Optional String id, String duration, @Default("0") int amountOff, @Optional String currency, @Default("0") int durationInMonths, @Default("0") int maxRedemptions, @Default("0") int percentOff, @Optional String redeemBy, @Default("#[payload]") Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	return couponClient.createCoupon(id, duration, amountOff, currency, durationInMonths, maxRedemptions, percentOff, redeemBy, metadata);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Coupon retrieveCoupon(String id) 
    		throws StripeConnectorException {
    	return couponClient.retrieveCoupon(id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Coupon updateCoupon(String id, @Default("#[payload]") Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	return couponClient.updateCoupon(id, metadata);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Object deleteCoupon(String id) 
    		throws StripeConnectorException {
    	return couponClient.deleteCoupon(id);
    }
    
    /**
     * List all Coupons
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-coupons}
     *
     * @param listAllCouponsParameters A wrapper class for the parameters
     * @return A Map with a data property that contains an array of up to limit coupons, starting after coupon starting_after. Each entry in the array is a separate coupon object. If no more coupons are available, the resulting array will be empty. This request should never throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public CouponCollection listAllCoupons(@Default("#[payload]") ListAllCouponsParameters listAllCouponsParameters) 
    		throws StripeConnectorException {
    	return couponClient.listAllCoupons(listAllCouponsParameters.getCreatedTimestamp(), listAllCouponsParameters.getCreated(), listAllCouponsParameters.getEndingBefore(), listAllCouponsParameters.getLimit(), listAllCouponsParameters.getStartingAfter());
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Balance retrieveBalance()    
    		throws StripeConnectorException {
    	return balanceClient.retrieveBalance();
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public BalanceTransaction retrieveBalanceTransaction(String id) 
    		throws StripeConnectorException {
    	return balanceClient.retrieveBalanceTransaction(id);
    }
    
    /**
     * List all Balance History
     *
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-balance-history}
     * 
     * @param listAllBalanceHistoryParameters Wrapper object for list all balance history parameters
     * @return Returns a balance object for the API key used.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public BalanceTransactionCollection listAllBalanceHistory(@Default("#[payload]") ListAllBalanceHistoryParameters listAllBalanceHistoryParameters)    
    		throws StripeConnectorException {
    	return balanceClient.listAllBalanceHistory(listAllBalanceHistoryParameters.getAvailableOnTimestamp(), listAllBalanceHistoryParameters.getAvailableOn(), listAllBalanceHistoryParameters.getCreatedTimestamp(), listAllBalanceHistoryParameters.getCreated(), listAllBalanceHistoryParameters.getCurrency(), listAllBalanceHistoryParameters.getEndingBefore(), listAllBalanceHistoryParameters.getLimit(), listAllBalanceHistoryParameters.getSourceId(), listAllBalanceHistoryParameters.getStartingAfter(), listAllBalanceHistoryParameters.getTransfer(), listAllBalanceHistoryParameters.getType());
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Card createCard(String ownerId, @Optional String sourceToken, @Default("#[payload]") Source source)    
    		throws StripeConnectorException {
    	return cardClient.createCard(ownerId, sourceToken, source);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Card retrieveCard(String ownerId, String id)    
    		throws StripeConnectorException {
    	return cardClient.retrieveCard(ownerId, id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Card updateCard(String ownerId, String id, @Optional String addressCity, @Optional String addressCountry, @Optional String addressLine1, @Optional String addressLine2, @Optional String addressState, @Optional String addressZip, @Optional String expMonth, @Optional String expYear, @Default("#[payload]") Map<String, Object> metadata, @Optional String cardName)    
    		throws StripeConnectorException {
    	return cardClient.updateCard(ownerId, id, addressCity, addressCountry, addressLine1, addressLine2, addressState, addressZip, expMonth, expYear, metadata, cardName);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public DeletedCard deleteCard(String ownerId, String id)    
    		throws StripeConnectorException {
    	return cardClient.deleteCard(ownerId, id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public PaymentSourceCollection listAllCustomerCards(String ownerId, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	return cardClient.listAllCustomerCards(ownerId, endingBefore, limit, startingAfter);
    }
    
    /**
     * Create a Charge
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-charge}
     * 
     * @param createChargeParameters Wrapper object for Create Charge Parameters
     * @return Returns the created Charge.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Charge createCharge(@Default("#[payload]") CreateChargeParameters createChargeParameters)    
    		throws StripeConnectorException {
    	return chargeClient.createCharge(createChargeParameters.getAmount(), createChargeParameters.getCurrency(), createChargeParameters.getCustomerId(), createChargeParameters.getSource(), createChargeParameters.getDescription(), createChargeParameters.getMetadata(), createChargeParameters.isCapture(), createChargeParameters.getStatementDescriptor(), createChargeParameters.getReceiptEmail(), createChargeParameters.getDestination(), createChargeParameters.getApplicationFee(), createChargeParameters.getShipping());
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Charge retrieveCharge(String id)    
    		throws StripeConnectorException {
    	return chargeClient.retrieveCharge(id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Charge updateCharge(String id, @Optional String description, @Default("#[payload]") Map<String, Object> metadata, @Optional String receiptEmail, @Optional Map<String, String> fraudDetails)    
    		throws StripeConnectorException {
    	return chargeClient.updateCharge(id, description, metadata, receiptEmail, fraudDetails);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Charge captureCharge(String id, @Default("0") int amount, @Default("0") int applicationFee, @Optional String statementDescriptor, @Optional String receiptEmail)    
    		throws StripeConnectorException {
    	return chargeClient.captureCharge(id, amount, applicationFee, statementDescriptor, receiptEmail);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public ChargeCollection listAllCharges(@Optional String createdTimestamp, @Default("#[payload]") TimeRange created, @Optional String customer, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	return chargeClient.listAllCharges(createdTimestamp, created, customer, endingBefore, limit, startingAfter);
    }
    
    /**
     * Create a Subscription
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-subscription}
     * 
     * @param createSubscriptionParameters The Wrapped Subscription Parameters
     * @return Returns the newly created subscription object if the call succeeded. If the customer has no card or the attempted charge fails, this call throws an error (unless the specified plan is free or has a trial period).
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Subscription createSubscription(@Default("#[payload]") CreateSubscriptionParameters createSubscriptionParameters)    
    		throws StripeConnectorException {
    	return subClient.createSubscription(createSubscriptionParameters.getCustomerId(), createSubscriptionParameters.getPlan(), createSubscriptionParameters.getCoupon(), createSubscriptionParameters.getTrialEnd(), createSubscriptionParameters.getSourceToken(), createSubscriptionParameters.getSource(), createSubscriptionParameters.getQuantity(), createSubscriptionParameters.getApplicationFeePercent(), createSubscriptionParameters.getTaxPercent(), createSubscriptionParameters.getMetadata());
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Subscription retrieveSubscription(String customerId, String subscriptionId)    
    		throws StripeConnectorException {
    	return subClient.retrieveSubscription(customerId, subscriptionId);
    }
    
    /**
     * Update a Subscription
     * Note that Stripe uses a specific policy around prorating changed subscriptions. If you want to use this endpoint, consider the documentation at https://stripe.com/docs/api/java#update_subscription closely.
     * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-subscription}
     * 
     * @param updateSubscriptionParameters Wrapper for the complex Update Subscription Parameters
     * @return Returns the updated Subscription
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Subscription updateSubscription(@Default("#[payload]") UpdateSubscriptionParameters updateSubscriptionParameters)    
    		throws StripeConnectorException {
    	return subClient.updateSubscription(updateSubscriptionParameters.getCustomerId(), updateSubscriptionParameters.getSubscriptionId(), updateSubscriptionParameters.getPlan(), updateSubscriptionParameters.getCoupon(), updateSubscriptionParameters.isProrate(), updateSubscriptionParameters.getTrialEnd(), updateSubscriptionParameters.getSourceToken(), updateSubscriptionParameters.getSource(), updateSubscriptionParameters.getQuantity(), updateSubscriptionParameters.getApplicationFeePercent(), updateSubscriptionParameters.getTaxPercent(), updateSubscriptionParameters.getMetadata());
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Subscription cancelSubscription(String customerId, String subscriptionId)    
    		throws StripeConnectorException {
    	return subClient.cancelSubscription(customerId, subscriptionId);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public CustomerSubscriptionCollection listActiveSubscriptions(String customerId, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	return subClient.listActiveSubscriptions(customerId, endingBefore, limit, startingAfter);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Refund createRefund(String id, @Default("0") int amount, @Default("false") boolean refundApplicationFee, @Optional String reason, @Default("#[payload]") Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	return refundClient.createRefund(id, amount, refundApplicationFee, reason, metadata);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Refund retrieveRefund(String id, String chargeId)    
    		throws StripeConnectorException {
    	return refundClient.retrieveRefund(id, chargeId);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Refund updateRefund(String id, String chargeId, @Default("#[payload]") Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	return refundClient.updateRefund(id, chargeId, metadata);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public ChargeRefundCollection listAllRefunds(String chargeId, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)    
    		throws StripeConnectorException {
    	return refundClient.listAllRefunds(chargeId, endingBefore, limit, startingAfter);
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
     * @param description The invoice description
     * @param metadata Arbitrary key-value pairs to attach to the invoice
     * @param statementDescriptor Extra information about a charge for the customer’s credit card statement.
     * @param subscription The ID of the subscription to invoice. If not set, the created invoice will include all pending invoice items for the customer. If set, the created invoice will exclude pending invoice items that pertain to other subscriptions.
     * @param taxPercent The percent tax rate applied to the invoice, represented as a decimal number.
     * @return Returns the invoice object if there are pending invoice items to invoice. Throws an error if there are no pending invoice items or if the customer ID provided is invalid.    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Invoice createInvoice(String customerId, @Default("0") int applicationFee, @Optional String description, @Default("#[payload]") Map<String, Object> metadata, @Optional String statementDescriptor, @Optional String subscription, @Default("0.0") double taxPercent)    
    		throws StripeConnectorException {
    	return invoiceClient.createInvoice(customerId, applicationFee, description, metadata, statementDescriptor, subscription, taxPercent);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Invoice retrieveInvoice(String id)    
    		throws StripeConnectorException {
    	return invoiceClient.retrieveInvoice(id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public InvoiceLineItemCollection retrieveInvoiceLineItems(String id, @Optional String customer, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter, @Optional String subscription)    
    		throws StripeConnectorException {
    	return invoiceClient.retrieveInvoiceLineItems(id, customer, endingBefore, limit, startingAfter, subscription);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Invoice retrieveUpcomingInvoice(String customerId, @Optional String subscription)    
    		throws StripeConnectorException {
    	return invoiceClient.retrieveUpcomingInvoice(customerId, subscription);
    }
    
    /**
     * Update an invoice
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-invoice}
     * 
     * @param invoiceId The invoice to update
     * @param applicationFee A fee in cents that will be applied to the invoice and transferred to the application owner’s Stripe account.
     * @param closed Boolean representing whether an invoice is closed or not. To close an invoice, pass true.
     * @param description The invoice description
     * @param forgiven Boolean representing whether an invoice is forgiven or not. To forgive an invoice, pass true. Forgiving an invoice instructs us to update the subscription status as if the invoice were succcessfully paid. Once an invoice has been forgiven, it cannot be unforgiven or reopened.    
     * @param metadata Arbitrary key-value pairs to attach to the invoice
     * @param statementDescriptor Extra information about a charge for the customer’s credit card statement.
     * @param taxPercent The percent tax rate applied to the invoice, represented as a decimal number.
     * @return Returns the invoice object     
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Invoice updateInvoice(String invoiceId, @Default("0") int applicationFee, @Default("false") boolean closed, @Optional String description, @Default("false") boolean forgiven, @Default("#[payload]") Map<String, Object> metadata, @Optional String statementDescriptor, @Default("0.0") double taxPercent)    
    		throws StripeConnectorException {
    	return invoiceClient.updateInvoice(invoiceId, applicationFee, closed, description, forgiven, metadata, statementDescriptor, taxPercent);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Invoice payInvoice(String id)    
    		throws StripeConnectorException {
    	return invoiceClient.payInvoice(id);
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
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public InvoiceCollection retrieveAllInvoices(@Optional String customerId, @Optional String dateTimestamp, @Default("#[payload]") TimeRange date, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)
    		throws StripeConnectorException {
    	return invoiceClient.retrieveAllInvoices(customerId, dateTimestamp, date, endingBefore, limit, startingAfter);
    }
    
    /**
     * Retrieve an Application Fee
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-application-fee}
     * 
     * @param id The identifier of the fee to be retrieved.
     * @return Returns Application Fee
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public ApplicationFee retrieveApplicationFee(String id)
    		throws StripeConnectorException {
    	return feeClient.retrieveApplicationFee(id);
    }
    
    /**
     * List all Application Fees
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-application-fees}
     * 
     * @param charge Only return application fees for the charge specified by this charge ID.
     * @param createdTimestamp A filter on the list based on the created field. The value can be a string with an integer Unix timestamp,...
     * @param created or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns Application Fee
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public ApplicationFeeCollection listAllApplicationFees(@Optional String charge, @Optional String createdTimestamp, @Default("#[payload]") TimeRange created, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)
    		throws StripeConnectorException {
    	return feeClient.listAllApplicationFees(charge, createdTimestamp, created, endingBefore, limit, startingAfter);
    }
    
    /**
     * Create an Account
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-account}
     * 
     * @param createAccountParameters Wrapper for the complex parameter set
     * @return Returns an account object if the call succeeded.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Account createAccount(@Default("#[payload]") CreateAccountParameters createAccountParameters)
    		throws StripeConnectorException {
    	return accountClient.createAccount(createAccountParameters.isManaged(), createAccountParameters.getCountry(), createAccountParameters.getEmail(), createAccountParameters.getBusinessName(), createAccountParameters.getBusinessUrl(), createAccountParameters.getSupportPhone(), createAccountParameters.getBankAccount(), createAccountParameters.isDebitNegativeBalances(), createAccountParameters.getDefaultCurrency(), createAccountParameters.getLegalEntity(), createAccountParameters.getProductDescription(), createAccountParameters.getStatementDescriptor(), createAccountParameters.getTosAcceptance(), createAccountParameters.getTransferSchedule(), createAccountParameters.getMetadata());
    }
    
    /**
     * Retrieve an Account's Details
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-account}
     * 
     * @param id The identifier of the account to be retrieved.
     * @return Returns Account
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Account retrieveAccount(String id)
    		throws StripeConnectorException {
    	return accountClient.retrieveAccount(id);
    }
    
    /**
     * Update an Account
     * You may only update accounts that you manage. To update your own account, you can currently only do so via the dashboard. 
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-account}
     * 
     * @param updateAccountParameters Wrapper for the Update Account Parameters
     * @return Returns an account object if the call succeeded.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Account updateAccount(@Default("#[payload]") UpdateAccountParameters updateAccountParameters)
    		throws StripeConnectorException {
    	return accountClient.updateAccount(updateAccountParameters.getId(), updateAccountParameters.getEmail(), updateAccountParameters.getBusinessName(), updateAccountParameters.getBusinessUrl(), updateAccountParameters.getSupportPhone(), updateAccountParameters.getBankAccount(), updateAccountParameters.isDebitNegativeBalances(), updateAccountParameters.getDefaultCurrency(), updateAccountParameters.getLegalEntity(), updateAccountParameters.getProductDescription(), updateAccountParameters.getStatementDescriptor(), updateAccountParameters.getTosAcceptance(), updateAccountParameters.getTransferSchedule(), updateAccountParameters.getMetadata());
    }
    
    /**
     * Create a Card Token
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-card-token}
     * 
     * @param cardId The card this token will represent. If you also pass in a customer, the card must be the ID of a card belonging to the customer... 
     * @param card ... Otherwise, if you do not pass a customer, a Map containing a user's credit card details
     * @param customer For use with Stripe Connect only; this can only be used with an OAuth access token or Stripe-Account header.. For more details, see the shared customers documentation. A customer (owned by the application's account) to create a token for.
     * @return The created card token object is returned if successful. Otherwise, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Token createCardToken(@Optional String cardId, @Default("#[payload]") Source card, @Optional String customer)
    		throws StripeConnectorException {
    	return tokenClient.createCardToken(cardId, card, customer);
    }
    
    /**
     * Create a Bank Account Token
     * Creates a single use token that wraps the details of a bank account. This token can be used in place of a bank account Map with any API method. These tokens can only be used once: by attaching them to a recipient.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-bank-account-token}
     * 
     * @param bankAccountId The bank account's ID, or...
     * @param bankAccount ... Otherwise, if you do not pass a bank account ID, a Map containing a the account details    
     * @return The created bank account token object is returned if successful. Otherwise, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Token createBankAccountToken(@Optional String bankAccountId, @Default("#[payload]") BankAccount bankAccount)
    		throws StripeConnectorException {
    	return tokenClient.createBankAccountToken(bankAccountId, bankAccount);
    }
    
    /**
     * Retrieve a Token
     * Retrieves the token with the given ID
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-token}
     * 
     * @param id	The ID of the desired token
     * @return Returns a token if a valid ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Token retrieveToken(String id)
    		throws StripeConnectorException {
    	return tokenClient.retrieveToken(id);
    }
    
    /**
     * Retrieve a Event
     * Retrieves the Event with the given ID
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-event}
     * 
     * @param id	The ID of the desired event
     * @return Returns a event if a valid ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public Event retrieveEvent(String id)
    		throws StripeConnectorException {
    	return eventClient.retrieveEvent(id);
    }
    
    /**
     * List All Events
     * List events, going back up to 30 days.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-events}
     * 
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp
     * @param created or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @param type A string containing a specific event name, or group of events using * as a wildcard. The list will be filtered to include only events with a matching event property
     * @return A Map with a data property that contains an array of up to limit events, starting after event starting_after, sorted in reverse chronological order. Each entry in the array is a separate event object. If no more events are available, the resulting array will be empty. This request should never throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public EventCollection listAllEvents(@Optional String createdTimestamp, @Default("#[payload]") TimeRange created, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter, @Optional String type)
    		throws StripeConnectorException {
    	return eventClient.listAllEvents(createdTimestamp, created, endingBefore, limit, startingAfter, type);
    }
    
    /**
     * Create an Application Fee Refund
     * Refunds an application fee that has previously been collected but not yet refunded. Funds will be refunded to the Stripe account that the fee was originally collected from.
	 * You can optionally refund only part of an application fee. You can do so as many times as you wish until the entire fee has been refunded.
	 * Once entirely refunded, an application fee can't be refunded again. This method will throw an error when called on an already-refunded application fee, or when trying to refund more money than is left on an application fee.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-application-fee-refund}
     * 
     * @param id The identifier of the application fee to be refunded.
     * @param amount A positive integer in cents representing how much of this fee to refund. Can only refund up to the unrefunded amount remaining of the fee.
     * @return Returns the application fee refund object if the refund succeeded. Throws an error if the fee has already been refunded or an invalid fee identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public ApplicationFee createApplicationFeeRefund(String id, @Default("0") int amount)
    		throws StripeConnectorException {
    	return feeClient.createApplicationFeeRefund(id, amount);
    }
    
    /**
     * Retrieve an Application Fee Refund
     * By default, you can see the 10 most recent refunds stored directly on the application fee object, but you can also retrieve details about a specific refund stored on the application fee.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-application-fee-refund}
     * 
     * @param id The ID of the refund to retrieve
     * @param fee ID of the Application Fee refunded
     * @return Returns the application fee refund object if the refund succeeded. Throws an error if the fee has already been refunded or an invalid fee identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public FeeRefund retrieveApplicationFeeRefund(String id, String fee)
    		throws StripeConnectorException {
    	return feeClient.retrieveApplicationFeeRefund(id, fee);
    }
    
    /**
     * Update an Application Fee Refund
     * Updates the specified application fee refund by setting the values of the parameters passed. Any parameters not provided will be left unchanged.
	 * This request only accepts metadata as an argument.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:update-application-fee-refund}
     * 
     * @param id The identifier of the refund.
     * @param fee The identifier of the Application Fee refunded
     * @param metadata A set of key/value pairs that you can attach to a refund object. It can be useful for storing additional information about the refund in a structured format. You can unset an individual key by setting its value to null and then saving.
     * @return Returns the application fee refund object if the refund succeeded. Throws an error if the fee has already been refunded or an invalid fee identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public FeeRefund updateApplicationFeeRefund(String id, String fee, @Default("#[payload]") Map<String, Object> metadata)
    		throws StripeConnectorException {
    	return feeClient.updateApplicationFeeRefund(id, fee, metadata);
    }
    
    /**
     * List All Application Fee Refunds
     * You can see a list of the refunds belonging to a specific application fee. Note that the 10 most recent refunds are always available by default on the application fee object. If you need more than those 10, you can use this API method and the limit and starting_after parameters to page through additional refunds
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-application-fee-refunds}
     * 
     * @param id The ID of the application fee whose refunds will be retrieved.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return A Map with a data property that contains an array of up to limit refunds, starting after starting_after. Each entry in the array is a separate application fee refund object. If no more refunds are available, the resulting array will be empty. If you provide a non-existent application fee ID, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public FeeRefundCollection listAllApplicationFeeRefunds(String id, @Optional String endingBefore, @Default("0") int limit, @Optional String startingAfter)
    		throws StripeConnectorException {
    	return feeClient.listAllApplicationFeeRefunds(id, endingBefore, limit, startingAfter);
    }
    
    /**
     * Create a BitCoin Receiver
     * Creates a Bitcoin receiver object that can be used to accept bitcoin payments from your customer. The receiver exposes a Bitcoin address and is created with a bitcoin to USD exchange rate that is valid for 10 minutes.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-bitcoin-receiver}
     * 
     * @param amount The amount of currency that you will be paid
     * @param currency The currency to which the bitcoin will be converted. You will be paid out in this currency. Only USD is currently supported.
     * @param email The email address of the customer.
     * @param description A description of the receiver
     * @param metadata A set of key/value pairs that you can attach to a customer object. It can be useful for storing additional information about the customer in a structured format.
     * @param refundMispayments A flag that indicates whether you would like Stripe to automatically handle refunds for any mispayments to the receiver.
     * @return Returns a Bitcoin receiver object if the call succeeded. The returned object will include the Bitcoin address of the receiver and the bitcoin amount required to fill it. The call will throw an error if a currency other than USD or an amount greater than $15,000.00 is specified.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public BitcoinReceiver createBitcoinReceiver(int amount, @Default("USD") String currency, String email, @Optional String description, @Default("#[payload]") Map<String, Object> metadata, @Default("false") boolean refundMispayments)
    		throws StripeConnectorException {
    	return bitcoinClient.createBitcoinReceiver(amount, currency, email, description, metadata, refundMispayments);
    }
    
    /**
     * Retrieve a Bitcoin Receiver
     * Retrieves the Bitcoin receiver with the given ID.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:retrieve-bitcoin-receiver}
     * 
     * @param id The ID of the receiver to retrieve
     * @return Returns a Bitcoin receiver if a valid ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public BitcoinReceiver retrieveBitcoinReceiver(String id)
    		throws StripeConnectorException {
    	return bitcoinClient.retrieveBitcoinReceiver(id);
    }
    
    /**
     * List All Bitcoin Receivers
     * Returns a list of your receivers. Receivers are returned sorted by creation date, with the most recently created receivers appearing first.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:list-all-bitcoin-receivers}
     * 
     * @param active Filter for active receivers.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param filled Filter for filled receivers.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @param uncapturedFunds Filter for receivers with uncaptured funds.
     * @return A Map with a data property that contains an array of up to limit Bitcoin receivers, starting after receiver starting_after. Each entry in the array is a separate Bitcoin receiver object. If no more receivers are available, the resulting array will be empty. This request should never throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public BitcoinReceiverCollection listAllBitcoinReceivers(@Optional String active, @Optional String endingBefore, @Optional String filled, @Default("0") int limit, @Optional String startingAfter, @Optional String uncapturedFunds)
    		throws StripeConnectorException {
    	return bitcoinClient.listAllBitcoinReceivers(active, endingBefore, filled, limit, startingAfter, uncapturedFunds);
    }
    
    /**
     * Create a file upload
     * To upload a file to Stripe, you'll need to send a request of type multipart/form-data. The request should contain the file you would like to upload, as well as the parameters for creating a file.
	 * 
     * {@sample.xml ../../../doc/stripe-connector.xml.sample stripe:create-file-upload}
     * 
     * @param file A file to upload. The file should follow the specifications of RFC 2388 (which defines file transfers for the multipart/form-data protocol).
     * @param purpose The purpose of the uploaded file. Possible values are identity_document, dispute_evidence.
     * @return Returns the file object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { StripeConnectorException.class })
    public FileUpload createFileUpload(String file, FilePurpose purpose)
    		throws StripeConnectorException {
    	return fileClient.createFileUpload(file, purpose);
    }
    
    
    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }
    
    /**
     * Resolves a nullpayload
     * @param source The source datatype
     * @param result The result datatype
     * @param muleContext The muleContext at the time
     * @return the resolver required
     * @throws Exception
     */
    @TransformerResolver
    public static org.mule.api.transformer.Transformer transformerResolver(DataType source, DataType result,
                        MuleContext muleContext) throws Exception {
        if (source.getType().equals(NullPayload.class)) {
            if (result.getType().equals(Map.class)) {
                NullPayloadToEmptyMapTransformer nullPayloadToEmptyMapTransformer = new NullPayloadToEmptyMapTransformer();
                muleContext.getRegistry().applyProcessorsAndLifecycle(nullPayloadToEmptyMapTransformer);
                return nullPayloadToEmptyMapTransformer;
            }
        }
        return null;
    }
    
}