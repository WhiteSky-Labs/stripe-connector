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
import com.stripe.model.Token;
import com.wsl.modules.stripe.complextypes.BankAccount;
import com.wsl.modules.stripe.complextypes.Source;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Token-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeTokenClient {
    /**
     * Create a Card Token
	 * 
     * @param cardId The card this token will represent. If you also pass in a customer, the card must be the ID of a card belonging to the customer... 
     * @param card ... Otherwise, if you do not pass a customer, a Map containing a user's credit card details
     * @param customer For use with Stripe Connect only; this can only be used with an OAuth access token or Stripe-Account header.. For more details, see the shared customers documentation. A customer (owned by the application's account) to create a token for.
     * @return The created card token object is returned if successful. Otherwise, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Token createCardToken(String cardId, Source card, String customer)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (cardId != null && !cardId.isEmpty()){
    		params.put("card", cardId);
    	} else if (card != null) {
    		params.put("card", card.toDictionary());
    	}
    	params.put("customer", customer);
    	params = StripeClientUtils.removeOptionals(params);
    	try {    		
    		return Token.create(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a Card Token", e);
		}
    }
    
    /**
     * Create a Bank Account Token
     * Creates a single use token that wraps the details of a bank account. This token can be used in place of a bank account Map with any API method. These tokens can only be used once: by attaching them to a recipient.
	 * 
     * @param bankAccountId The bank account's ID, or...
     * @param bankAccount ... Otherwise, if you do not pass a bank account ID, a Map containing a the account details    
     * @return The created bank account token object is returned if successful. Otherwise, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Token createBankAccountToken(String bankAccountId, BankAccount bankAccount)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (bankAccountId != null && !bankAccountId.isEmpty()){
    		params.put("bank_account", bankAccountId);
    	} else if (bankAccount != null) {
    		params.put("bank_account", bankAccount.toDictionary());
    	}
    	try {    		
    		return Token.create(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a Bank Account Token", e);
		}
    }
    
    /**
     * Retrieve a Token
     * Retrieves the token with the given ID
	 * 
     * @param id	The ID of the desired token
     * @return Returns a token if a valid ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Token retrieveToken(String id)
    		throws StripeConnectorException {
    	try {    		
    		return Token.retrieve(id);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Token", e);
		}
    }
}
