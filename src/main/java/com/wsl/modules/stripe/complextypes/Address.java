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
 * Represents a Stripe Address INPUT object
 * @author WhiteSky Labs
 *
 */
public class Address {
	private String line1;
	private String line2;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	/**
	 * @return the line1
	 */
	public String getLine1() {
		return line1;
	}
	/**
	 * @param line1 the line1 to set
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	/**
	 * @return the line2
	 */
	public String getLine2() {
		return line2;
	}
	/**
	 * @param line2 the line2 to set
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
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
	 * Turns the object into a Stripe API-compliant dictionary with the correct values.
	 * 
	 * @return A map that represents the JSON dictionary.
	 */
	public Map<String, Object> toDictionary(){
		Map<String, Object> dict = new HashMap<String, Object>();
		dict.put("line1", getLine1());
		dict.put("line2", getLine2());
		dict.put("city", getCity());
		dict.put("state", getState());
		dict.put("postal_code", getPostalCode());
		dict.put("country", getCountry());
		return StripeClientUtils.removeOptionals(dict);
	}
}
