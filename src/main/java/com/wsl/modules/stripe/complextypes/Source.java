/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
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
 * Source represents the inputs for a source type. 
 * Datamapper seems to have trouble interpreting the native Stripe types, so this removes the extraneous overhead to simplify.
 * 
 * @author WhiteSky Labs
 *
 */
public class Source implements java.io.Serializable{
	private String number;
	private String expMonth;
	private String expYear;
	private String cvc;
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String addressCity;
	private String addressState;
	private String addressZip;
	private String addressCountry;
	
	/**
	 * @return the cvc
	 */
	public String getCvc() {
		return cvc;
	}
	/**
	 * @param cvc the cvc to set
	 */
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}
	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}
	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	
	/**
	 * @return the addressCity
	 */
	public String getAddressCity() {
		return addressCity;
	}
	/**
	 * @param addressCity the addressCity to set
	 */
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	/**
	 * @return the addressState
	 */
	public String getAddressState() {
		return addressState;
	}
	/**
	 * @param addressState the addressState to set
	 */
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	/**
	 * @return the addressZip
	 */
	public String getAddressZip() {
		return addressZip;
	}
	/**
	 * @param addressZip the addressZip to set
	 */
	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}
	/**
	 * @return the addressCountry
	 */
	public String getAddressCountry() {
		return addressCountry;
	}
	/**
	 * @param addressCountry the addressCountry to set
	 */
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the expMonth
	 */
	public String getExpMonth() {
		return expMonth;
	}
	/**
	 * @param expMonth the expMonth to set
	 */
	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}
	/**
	 * @return the expYear
	 */
	public String getExpYear() {
		return expYear;
	}
	/**
	 * @param expYear the expYear to set
	 */
	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}
	
	/**
	 * Turns the object into a Stripe API-compliant dictionary with the correct values.
	 * 
	 * @return A map that represents the JSON dictionary.
	 */
	public Map<String, Object> toDictionary(){
		Map<String, Object> dict = new HashMap<String, Object>();
		dict.put("object", "card");
		dict.put("number", this.getNumber());
		dict.put("exp_month", this.getExpMonth());
		dict.put("exp_year", this.getExpYear());
		dict.put("cvc", this.getCvc());
		dict.put("name", this.getName());
		dict.put("address_line1", this.getAddressLine1());
		dict.put("address_line2", this.getAddressLine2());
		dict.put("address_city", this.getAddressCity());
		dict.put("address_state", this.getAddressState());
		dict.put("address_zip", this.getAddressZip());
		dict.put("address_country", this.getAddressCountry());
		return StripeClientUtils.removeOptionalsAndZeroes(dict);
	}
}
