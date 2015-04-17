/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe.complextypes;

import java.util.HashMap;
import java.util.Map;

import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Represents a Bank Account input in Stripe
 * @author WhiteSky Labs
 *
 */
public class BankAccount implements java.io.Serializable{
	private String country;
	private String currency;
	private String routingNumber;
	private String accountNumber;
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the routingNumber
	 */
	public String getRoutingNumber() {
		return routingNumber;
	}
	/**
	 * @param routingNumber the routingNumber to set
	 */
	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * Turns the object into a Stripe API-compliant dictionary with the correct values.
	 * 
	 * @return A map that represents the JSON dictionary.
	 */
	public Map<String, Object> toDictionary(){
		Map<String, Object> dict = new HashMap<String, Object>();
		dict.put("country", getCountry());
		dict.put("currency", getCurrency());
		dict.put("routing_number", getRoutingNumber());
		dict.put("account_number", getAccountNumber());
		return StripeClientUtils.removeOptionals(dict);
	}
}
