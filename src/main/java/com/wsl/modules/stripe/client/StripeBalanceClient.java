package com.wsl.modules.stripe.client;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Balance;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Balance-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeBalanceClient {
    /**
     * Retrieve the balance
     *
     * @return Returns a balance object for the API key used.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
     * @param id The id of the transaction
     * @return Returns a balance transaction if a valid balance transaction ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
    public BalanceTransactionCollection listAllBalanceHistory(String availableOnTimestamp, Map<String, String> availableOn, String createdTimestamp, Map<String, String> created, String currency, String endingBefore, int limit, String sourceId, String startingAfter, String transfer, String type)    
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
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		return BalanceTransaction.all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the balance history", e);
		}
    }
}
