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
import com.stripe.model.Account;
import com.stripe.net.RequestOptions;
import com.wsl.modules.stripe.complextypes.Acceptance;
import com.wsl.modules.stripe.complextypes.BankAccount;
import com.wsl.modules.stripe.complextypes.LegalEntity;
import com.wsl.modules.stripe.complextypes.TransferSchedule;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Account-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeAccountClient {
    /**
     * Create an Account
	 * 
     * @param managed Whether you'd like to create a managed or standalone account. Managed accounts have extra parameters available to them, and require that you, the platform, handle all communication with the account holder. Standalone accounts are normal Stripe accounts: Stripe will email the account holder to setup a username and password, and handle all account management directly with them.
     * @param country The country the account holder resides in or that the business is legally established in. For example, if you are in the United States and the business you’re creating an account for is legally represented in Canada, you would use “CA” as the country for the account being created.
     * @param email The email address of the account holder. For standalone accounts, Stripe will email your user with instructions for how to set up their account. For managed accounts, this is only to make the account easier to identify to you: Stripe will never directly reach out to your users.
     * @param businessName The publicly sharable name for this account
     * @param businessUrl The URL that best shows the service or product provided for this account
     * @param supportPhone A publicly shareable phone number that can be reached for support for this account
     * @param bankAccount A bank account to attach to the account. 
     * @param debitNegativeBalances A boolean for whether or not Stripe should try to reclaim negative balances from the account holder’s bank account. 
     * @param defaultCurrency Three-letter ISO currency code representing the default currency for the account.
     * @param legalEntity Information about the holder of this account, i.e. the user receiving funds from this account
     * @param productDescription Internal-only description of the product being sold or service being provided by this account. It’s used by Stripe for risk and underwriting purposes.
     * @param statementDescriptor The text that will appear on credit card statements by default if a charge is being made directly on the account.
     * @param tosAcceptance Details on who accepted the Stripe terms of service, and when they accepted it.
     * @param transferSchedule Details on when this account will make funds from charges available, and when they will be paid out to the account holder’s bank account. 
	 * @param metadata A set of key/value pairs that you can attach to an account object. It can be useful for storing additional information about the account in a structured format. This will be unset if you POST an empty value.  
     * @return Returns an account object if the call succeeded.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
   public Account createAccount(boolean managed, String country, String email, String businessName, String businessUrl, String supportPhone, BankAccount bankAccount, boolean debitNegativeBalances, String defaultCurrency, LegalEntity legalEntity, String productDescription, String statementDescriptor, Acceptance tosAcceptance, TransferSchedule transferSchedule, Map<String, Object> metadata)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("managed", managed);
    	params.put("country", country);
    	params.put("email", email);
    	params.put("business_name", businessName);
    	params.put("business_url", businessUrl);
    	params.put("support_phone", supportPhone);
    	if (bankAccount != null){
    		params.put("bank_account", bankAccount.toDictionary());
    	}
    	params.put("debit_negative_balances", debitNegativeBalances);
    	params.put("default_currency", defaultCurrency);
    	if (legalEntity != null){
    		params.put("legal_entity", legalEntity.toDictionary());
    	}
    	params.put("product_description", productDescription);
    	params.put("statement_descriptor", statementDescriptor);
    	if (tosAcceptance != null){
    		params.put("tos_acceptance", tosAcceptance.toDictionary());
    	}
    	if (transferSchedule != null){
    		params.put("transfer_schedule", transferSchedule.toDictionary());
    	}
    	params.put("metadata", metadata);
    	params = StripeClientUtils.removeOptionals(params);
    	try {    		
    		return Account.create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the Account", e);
		}
    }
    
    /**
     * Retrieve an Account's Details
	 * 
     * @param id The identifier of the account to be retrieved.
     * @return Returns Account
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Account retrieveAccount(String id)
    		throws StripeConnectorException {
    	try {    		
    		RequestOptions options = RequestOptions.getDefault();
    		return Account.retrieve(id, options);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Account", e);
		}
    }
    
    /**
     * Update an Account
     * You may only update accounts that you manage. To update your own account, you can currently only do so via the dashboard. 
	 *  
     * @param id The account to update
     * @param email The email address of the account holder. For standalone accounts, Stripe will email your user with instructions for how to set up their account. For managed accounts, this is only to make the account easier to identify to you: Stripe will never directly reach out to your users.
     * @param businessName The publicly sharable name for this account
     * @param businessUrl The URL that best shows the service or product provided for this account
     * @param supportPhone A publicly shareable phone number that can be reached for support for this account
     * @param bankAccount The bank account to associate with the account
     * @param debitNegativeBalances A boolean for whether or not Stripe should try to reclaim negative balances from the account holder’s bank account. 
     * @param defaultCurrency Three-letter ISO currency code representing the default currency for the account.
     * @param legalEntity Information about the holder of this account, i.e. the user receiving funds from this account
     * @param productDescription Internal-only description of the product being sold or service being provided by this account. It’s used by Stripe for risk and underwriting purposes.
     * @param statementDescriptor The text that will appear on credit card statements by default if a charge is being made directly on the account.
     * @param tosAcceptance Details on who accepted the Stripe terms of service, and when they accepted it.
     * @param transferSchedule Details on when this account will make funds from charges available, and when they will be paid out to the account holder’s bank account. 
	 * @param metadata A set of key/value pairs that you can attach to an account object. It can be useful for storing additional information about the account in a structured format. This will be unset if you POST an empty value.  
     * @return Returns an account object if the call succeeded.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Account updateAccount(String id, String email, String businessName, String businessUrl, String supportPhone, BankAccount bankAccount, boolean debitNegativeBalances, String defaultCurrency, LegalEntity legalEntity, String productDescription, String statementDescriptor, Acceptance tosAcceptance, TransferSchedule transferSchedule, Map<String, Object> metadata)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("email", email);
    	params.put("business_name", businessName);
    	params.put("business_url", businessUrl);
    	params.put("support_phone", supportPhone);
    	if (bankAccount != null){
    		params.put("bank_account", bankAccount.toDictionary());
    	}
    	params.put("debit_negative_balances", debitNegativeBalances);
    	params.put("default_currency", defaultCurrency);
    	if (legalEntity != null){
    		params.put("legal_entity", legalEntity.toDictionary());
    	}
    	params.put("product_description", productDescription);
    	params.put("statement_descriptor", statementDescriptor);
    	if (tosAcceptance != null){
    		params.put("tos_acceptance", tosAcceptance.toDictionary());
    	}
    	if (transferSchedule != null){
    		params.put("transfer_schedule", transferSchedule.toDictionary());
    	}    	
    	params.put("metadata", metadata);
    	params = StripeClientUtils.removeOptionals(params);
    	try {    	  
    		RequestOptions options = RequestOptions.getDefault();
    		return Account.retrieve(id, options).update(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the Account", e);
		}
    }
}
