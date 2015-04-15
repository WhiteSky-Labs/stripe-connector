/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

package com.wsl.modules.stripe.client;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.ApplicationFee;
import com.stripe.model.ApplicationFeeCollection;
import com.stripe.model.FeeRefund;
import com.stripe.model.FeeRefundCollection;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Application Fee-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeApplicationFeeClient {
	/**
     * Retrieve an Application Fee
	 * 
     * @param id The identifier of the fee to be retrieved.
     * @return Returns Application Fee
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public ApplicationFee retrieveApplicationFee(String id)
    		throws StripeConnectorException {
    	try {    		
    		return ApplicationFee.retrieve(id);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Application Fee", e);
		}
    }
    
    /**
     * List all Application Fees
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
    public ApplicationFeeCollection listAllApplicationFees(String charge, String createdTimestamp, Map<String, String> created, String endingBefore, int limit, String startingAfter)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("charge", charge);
    	if (createdTimestamp != null && !createdTimestamp.isEmpty()){
    		params.put("created", createdTimestamp);
    	} else {
    		params.put("created", created);
    	}
    	params.put("endingBefore", endingBefore);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {    		
    		return ApplicationFee.all(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list Application Fees", e);
		}
    }
    
    /**
     * Create an Application Fee Refund
     * Refunds an application fee that has previously been collected but not yet refunded. Funds will be refunded to the Stripe account that the fee was originally collected from.
	 * You can optionally refund only part of an application fee. You can do so as many times as you wish until the entire fee has been refunded.
	 * Once entirely refunded, an application fee can't be refunded again. This method will throw an error when called on an already-refunded application fee, or when trying to refund more money than is left on an application fee.
	 * 
     * @param id The identifier of the application fee to be refunded.
     * @param amount A positive integer in cents representing how much of this fee to refund. Can only refund up to the unrefunded amount remaining of the fee.
     * @return Returns the application fee refund object if the refund succeeded. Throws an error if the fee has already been refunded or an invalid fee identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public ApplicationFee createApplicationFeeRefund(String id, int amount)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("amount", amount);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		return ApplicationFee.retrieve(id).refund(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not refund Application Fee", e);
		}
    }
    
    /**
     * Retrieve an Application Fee Refund
     * By default, you can see the 10 most recent refunds stored directly on the application fee object, but you can also retrieve details about a specific refund stored on the application fee.
	 * 
     * @param id The ID of the refund to retrieve
     * @param fee ID of the Application Fee refunded
     * @param metadata A set of key/value pairs that you can attach to a refund object. It can be useful for storing additional information about the refund in a structured format. You can unset an individual key by setting its value to null and then saving.
     * @return Returns the application fee refund object if the refund succeeded. Throws an error if the fee has already been refunded or an invalid fee identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public FeeRefund retrieveApplicationFeeRefund(String id, String fee)
    		throws StripeConnectorException {
    	try {
    		return ApplicationFee.retrieve(fee).getRefunds().retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve Application Fee Refund", e);
		}
    }
    
    /**
     * Update an Application Fee Refund
     * Updates the specified application fee refund by setting the values of the parameters passed. Any parameters not provided will be left unchanged.
	 * This request only accepts metadata as an argument.
	 * 
     * @param id The identifier of the refund.
     * @param fee The identifier of the Application Fee refunded
     * @param metadata A set of key/value pairs that you can attach to a refund object. It can be useful for storing additional information about the refund in a structured format. You can unset an individual key by setting its value to null and then saving.
     * @return Returns the application fee refund object if the refund succeeded. Throws an error if the fee has already been refunded or an invalid fee identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public FeeRefund updateApplicationFeeRefund(String id, String fee, Map<String, Object> metadata)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("metadata", metadata);
    	try {
    		FeeRefund refund = ApplicationFee.retrieve(fee).getRefunds().retrieve(id);
    		return refund.update(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update Application Fee Refund", e);
		}
    }
    
    /**
     * List All Application Fee Refunds
     * You can see a list of the refunds belonging to a specific application fee. Note that the 10 most recent refunds are always available by default on the application fee object. If you need more than those 10, you can use this API method and the limit and starting_after parameters to page through additional refunds
	 * 
     * @param id The ID of the application fee whose refunds will be retrieved.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return A Map with a data property that contains an array of up to limit refunds, starting after starting_after. Each entry in the array is a separate application fee refund object. If no more refunds are available, the resulting array will be empty. If you provide a non-existent application fee ID, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public FeeRefundCollection listAllApplicationFeeRefunds(String id, String endingBefore, int limit, String startingAfter)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("ending_before", endingBefore);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		return ApplicationFee.retrieve(id).getRefunds().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list Application Fee Refunds", e);
		}
    }
}
