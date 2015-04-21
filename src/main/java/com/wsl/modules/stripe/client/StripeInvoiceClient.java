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
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceLineItemCollection;
import com.wsl.modules.stripe.complextypes.TimeRange;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Invoice-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeInvoiceClient {
    /**
     * Create an invoice
	 * Invoices are statements of what a customer owes for a particular billing period, including subscriptions, invoice items, and any automatic proration adjustments if necessary.
	 * Once an invoice is created, payment is automatically attempted. Note that the payment, while automatic, does not happen exactly at the time of invoice creation. If you have configured webhooks, the invoice will wait until one hour after the last webhook is successfully sent (or the last webhook times out after failing).
	 * Any customer credit on the account is applied before determining how much is due for that invoice (the amount that will be actually charged). If the amount due for the invoice is less than 50 cents (the minimum for a charge), we add the amount to the customer's running account balance to be added to the next invoice. If this amount is negative, it will act as a credit to offset the next invoice. Note that the customer account balance does not include unpaid invoices; it only includes balances that need to be taken into account when calculating the amount due for the next invoice.
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
    public Invoice createInvoice(String customerId, int applicationFee, String description, Map<String, Object> metadata, String statementDescriptor, String subscription, double taxPercent)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customerId);
    	params.put("application_fee", applicationFee);
    	params.put("description", description);
    	params.put("metadata", metadata);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("subscription", subscription);
    	params.put("tax_percent", taxPercent);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
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
     * @param id The identifier of the desired invoice
     * @return Returns the invoice requested.    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
     * @param id The id of the invoice containing the lines to be retrieved
     * @param customer In the case of upcoming invoices, the customer of the upcoming invoice is required. In other cases it is ignored.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @param subscription In the case of upcoming invoices, the subscription of the upcoming invoice is optional. In other cases it is ignored.
     * @return Returns the collection of invoice line items    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public InvoiceLineItemCollection retrieveInvoiceLineItems(String id, String customer, String endingBefore, int limit, String startingAfter, String subscription)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customer);
    	params.put("endingBefore", endingBefore);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params.put("subscription", subscription);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
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
     * @param customerId The identifier of the customer whose upcoming invoice you'd like to retrieve.
     * @param subscription The identifier of the subscription for which you'd like to retrieve the upcoming invoice. If not provided, you will retrieve the next upcoming invoice from among the customer's subscriptions.
     * @return Returns an invoice if a valid customer ID was provided. Throws an error otherwise.    
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Invoice retrieveUpcomingInvoice(String customerId, String subscription)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customerId);
    	params.put("subscription", subscription);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
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
    public Invoice updateInvoice(String invoiceId, int applicationFee, boolean closed, String description, boolean forgiven, Map<String, Object> metadata, String statementDescriptor, double taxPercent)    
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("application_fee", applicationFee);
    	params.put("closed", closed);
    	params.put("description", description);
    	params.put("forgiven", forgiven);
    	params.put("metadata", metadata);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("tax_percent", taxPercent);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
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
     * @param id the invoice id
     * @return Returns the invoice object     
     * @throws StripeConnectorException when there is a problem with the Connector
     */
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
     * @param customerId The id of the customer
     * @param dateTimestamp A filter on the list based on the object date field. The value can be a string with an integer Unix timestamp,...
     * @param date or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return Returns the collection of invoices
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public InvoiceCollection retrieveAllInvoices(String customerId, String dateTimestamp, TimeRange date, String endingBefore, int limit, String startingAfter)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("customer", customerId);
    	if (dateTimestamp != null && !dateTimestamp.isEmpty()){
    		params.put("date", dateTimestamp);
    	} else if (date != null){
    		params.put("date", date.toDict());
    	}
    	params.put("endingBefore", endingBefore);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {    		
    		return Invoice.all(params);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve Invoices", e);
		}
    }
}
