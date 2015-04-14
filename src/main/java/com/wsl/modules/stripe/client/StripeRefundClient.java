package com.wsl.modules.stripe.client;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.ChargeRefundCollection;
import com.stripe.model.Refund;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Refund-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeRefundClient {
    /**
     * Create a Refund
     * When you create a new refund, you must specify a charge to create it on.
	 * Creating a new refund will refund a charge that has previously been created but not yet refunded. Funds will be refunded to the credit or debit card that was originally charged. The fees you were originally charged are also refunded.
	 * You can optionally refund only part of a charge. You can do so as many times as you wish until the entire charge has been refunded.
	 * Once entirely refunded, a charge can't be refunded again. This method will throw an error when called on an already-refunded charge, or when trying to refund more money than is left on a charge.
     * 
     * @param id The identifier of the charge to be refunded.
     * @param amount A positive integer in cents representing how much of this charge to refund. Can only refund up to the unrefunded amount remaining of the charge.
     * @param refundApplicationFee Boolean indicating whether the application fee should be refunded when refunding this charge. If a full charge refund is given, the full application fee will be refunded. Else, the application fee will be refunded with an amount proportional to the amount of the charge refunded. An application fee can only be refunded by the application that created the charge.
     * @param reason String indicating the reason for the refund. If set, possible values are duplicate, fraudulent, and requested_by_customer. Specifying fraudulent as the reason when you believe the charge to be fraudulent will help us improve our fraud detection algorithms.
     * @param metadata A set of key/value pairs that you can attach to a refund object. It can be useful for storing additional information about the refund in a structured format. You can unset an individual key by setting its value to null and then saving. To clear all keys, set metadata to null, then save.
     * @return Returns the refund object if the refund succeeded. Throws an error if the charge has already been refunded or an invalid charge identifier was provided.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Refund createRefund(String id, int amount, boolean refundApplicationFee, String reason, Map<String, Object> metadata)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
    	params.put("refund_application_fee", refundApplicationFee);
    	params.put("reason", reason);
    	params.put("metadata", metadata);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
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
     * @param id The identifier of the refund
     * @param chargeId The identifier of the Charge refunded.
     * @return Returns Returns the refund object if found.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
     * @param id The identifier of the refund
     * @param chargeId The identifier of the Charge refunded.
     * @param metadata A set of key/value pairs that you can attach to a refund object. It can be useful for storing additional information about the refund in a structured format.
     * @return Returns the updated refund.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
     * @param chargeId The ID of the charge whose refunds will be retrieved.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns a list of the charge's refunds
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public ChargeRefundCollection listAllRefunds(String chargeId, String endingBefore, int limit, String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		Charge charge = Charge.retrieve(chargeId);
    		return charge.getRefunds().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the Refunds", e);
		}
    }
}
