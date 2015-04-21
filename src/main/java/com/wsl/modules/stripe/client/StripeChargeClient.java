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
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.wsl.modules.stripe.complextypes.Source;
import com.wsl.modules.stripe.complextypes.TimeRange;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Charge-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeChargeClient {
    /**
     * Create a Charge
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
    public Charge createCharge(int amount, String currency, String customerId, Source source, String description, Map<String, Object> metadata, boolean capture, String statementDescriptor, String receiptEmail, String destination, int applicationFee, Map<String, String> shipping)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	if (source != null){
    		Map<String, Object> sourceDict = source.toDictionary();
    		sourceDict = StripeClientUtils.removeOptionals(sourceDict);
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
		params = StripeClientUtils.removeOptionalsAndZeroes(params);
		
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
     * @param id The identifier of the charge to be retrieved.
     * @return Returns a charge object if a valid identifier was provided, and throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
     * @param id The id of the charge to update
     * @param description An arbitrary string which you can attach to a charge object. It is displayed when in the web interface alongside the charge. Note that if you use Stripe to send automatic email receipts to your customers, your receipt emails will include the description of the charge(s) that they are describing.
     * @param metadata A set of key/value pairs that you can attach to a charge object. It can be useful for storing additional information about the customer in a structured format. It's often a good idea to store an email address in metadata for tracking later.
     * @param receiptEmail The email address to send this charge's receipt to. The receipt will not be sent until the charge is paid. If this charge is for a customer, the email address specified here will override the customer's email address. Receipts will not be sent for test mode charges. If receipt_email is specified for a charge in live mode, a receipt will be sent regardless of your email settings.
     * @param fraudDetails A set of key/value pairs you can attach to a charge giving information about its riskiness. If you believe a charge is fraudulent, include a user_report key with a value of fraudulent. If you believe a charge is safe, include a user_report key with a value of safe. Note that you must refund a charge before setting the user_report to fraudulent. Stripe will use the information you send to improve our fraud detection algorithms.
     * @return Returns the created Charge.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Charge updateCharge(String id, String description, Map<String, Object> metadata, String receiptEmail, Map<String, String> fraudDetails)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("description", description);
		params.put("metadata", metadata);
		params.put("receipt_email", receiptEmail);
		params.put("fraud_details", fraudDetails);
		params = StripeClientUtils.removeOptionals(params);
		
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
     * @param id The id of the charge to capture
     * @param amount The amount to capture, which must be less than or equal to the original amount. Any additional amount will be automatically refunded.
     * @param applicationFee An application fee to add on to this charge. Can only be used with Stripe Connect. 
     * @param statementDescriptor An arbitrary string to be displayed on your customer's credit card statement. This may be up to 22 characters. As an example, if your website is RunClub and the item you're charging for is a race ticket, you may want to specify a statement_descriptor of RunClub 5K race ticket. The statement description may not include <>"' characters, and will appear on your customer's statement in capital letters. Non-ASCII characters are automatically stripped. While most banks display this information consistently, some may display it incorrectly or not at all.
     * @param receiptEmail The email the receipt should be sent to
     * @return Returns the charge object, with an updated captured property (set to true). Capturing a charge will always succeed, unless the charge is already refunded, expired, captured, or an invalid capture amount is specified, in which case this method will throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Charge captureCharge(String id, int amount, int applicationFee, String statementDescriptor, String receiptEmail)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	params.put("amount", amount);
		params.put("statement_descriptor", statementDescriptor);
		params.put("receipt_email", receiptEmail);
		params.put("application_fee", applicationFee);
		params = StripeClientUtils.removeOptionalsAndZeroes(params);
		
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
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp, 
     * @param created ... or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param customer Only return charges for the customer specified by this customer ID.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return A Map with a data property that contains an array of up to limit charges, starting after charge starting_after. Each entry in the array is a separate charge object. If no more charges are available, the resulting array will be empty. If you provide a non-existent customer ID, this call throws an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public ChargeCollection listAllCharges(String createdTimestamp, TimeRange created, String customer, String endingBefore, int limit, String startingAfter)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("limit", limit);
    	params.put("ending_before", endingBefore);
    	params.put("startingAfter", startingAfter);
    	params.put("customer", customer);
    	if (createdTimestamp != null && !createdTimestamp.isEmpty()){
    		params.put("created", createdTimestamp);
    	} else if (created != null) {
    		params.put("created", created.toDict());
    	}
    	
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		return Charge.all(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list the charges", e);
		}
    }
}
