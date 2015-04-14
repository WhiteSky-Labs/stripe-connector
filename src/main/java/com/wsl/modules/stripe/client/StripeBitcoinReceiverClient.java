package com.wsl.modules.stripe.client;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.BitcoinReceiver;
import com.stripe.model.BitcoinReceiverCollection;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Bitcoin Receiver-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeBitcoinReceiverClient {
    /**
     * Create a BitCoin Receiver
     * Creates a Bitcoin receiver object that can be used to accept bitcoin payments from your customer. The receiver exposes a Bitcoin address and is created with a bitcoin to USD exchange rate that is valid for 10 minutes.
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
    public BitcoinReceiver createBitcoinReceiver(int amount, String currency, String email, String description, Map<String, Object> metadata, boolean refundMispayments)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("amount", amount);
    	params.put("currency", currency);
    	params.put("email", email);
    	params.put("description", description);
    	params.put("metadata", metadata);
    	params.put("refund_mispayments", refundMispayments);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		return BitcoinReceiver.create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create Bitcoin Receiver", e);
		}
    }
    
    /**
     * Retrieve a Bitcoin Receiver
     * Retrieves the Bitcoin receiver with the given ID.
	 * 
     * @param id The ID of the receiver to retrieve
     * @return Returns a Bitcoin receiver if a valid ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public BitcoinReceiver retrieveBitcoinReceiver(String id)
    		throws StripeConnectorException {
    	try {
    		return BitcoinReceiver.retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve Bitcoin Receiver", e);
		}
    }
    
    /**
     * List All Bitcoin Receivers
     * Returns a list of your receivers. Receivers are returned sorted by creation date, with the most recently created receivers appearing first.
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
    public BitcoinReceiverCollection listAllBitcoinReceivers(String active, String endingBefore, String filled, int limit, String startingAfter, String uncapturedFunds)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("active", active);
    	params.put("ending_before", endingBefore);
    	params.put("filled", filled);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params.put("uncapturedFunds", uncapturedFunds);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		return BitcoinReceiver.all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list Bitcoin Receivers", e);
		}
    }
}
