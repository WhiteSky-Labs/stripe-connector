package com.wsl.modules.stripe.client;

import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.DeletedCard;
import com.stripe.model.PaymentSource;
import com.stripe.model.PaymentSourceCollection;
import com.wsl.modules.stripe.complextypes.Source;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Card-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeCardClient {
    /**
     * Create a Card
     * Note that this is only available for Customers at this time, as Recipients (of Transfers) have been deprecated by Stripe.
     * Stripe Connect is the recommended means for transferring funds.
     * 
     * @param ownerId the ID for the Customer or Recipient
     * @param sourceToken The source can either be a token, like the ones returned by Stripe.js, 
     * @param source or a dictionary containing a user’s credit card details (with the options shown below). Whenever you create a new card for a customer, Stripe will automatically validate the card.
     * @return Returns the created Card.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Card createCard(String ownerId, String sourceToken, Source source)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	if (sourceToken != null && !sourceToken.isEmpty()){
    		params.put("source", sourceToken);
    	} else if (source != null){
    		Map<String, Object> sourceDict = source.toDictionary();
    		sourceDict = StripeClientUtils.removeOptionals(sourceDict);
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
     * @param ownerId The ID of the owner of the card.
     * @param id The ID of the card to be retrieved.
     * @return Returns the retrieved Card.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
    public Card updateCard(String ownerId, String id, String addressCity, String addressCountry, String addressLine1, String addressLine2, String addressState, String addressZip, String expMonth, String expYear, Map<String, Object> metadata, String cardName)    
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
    	params = StripeClientUtils.removeOptionals(params);
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
     * @param ownerId The ID of the owner of the card.
     * @param id The ID of the card to be deleted.
     * @return Returns the deleted card object.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
     * @param ownerId The ID of the customer whose cards will be retrieved
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns a list of the cards stored on the customer or recipient.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public PaymentSourceCollection listAllCustomerCards(String ownerId, String endingBefore, int limit, String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		Customer customer = Customer.retrieve(ownerId);
    		return customer.getSources().all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the customer cards", e);
		}
    }
}
