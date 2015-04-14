/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe.complextypes;

import java.util.*;

/**
 * Represents a business Owner in the Stripe Input API
 * @author WhiteSky Labs
 *
 */
public class Owner {
	private Address address;
	private DateOfBirth dob;
	private String firstName;
	private String lastName;
	private String verificationDocumentId;
	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	/**
	 * @return the dob
	 */
	public DateOfBirth getDob() {
		return dob;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(DateOfBirth dob) {
		this.dob = dob;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the verificationDocumentId
	 */
	public String getVerificationDocumentId() {
		return verificationDocumentId;
	}
	/**
	 * @param verificationDocumentId the verificationDocumentId to set
	 */
	public void setVerificationDocumentId(String verificationDocumentId) {
		this.verificationDocumentId = verificationDocumentId;
	}
	/**
	 * Turns the object into a Stripe API-compliant dictionary with the correct values.
	 * 
	 * @return A map that represents the JSON dictionary.
	 */
	public Map<String, Object> toDictionary(){
		Map<String, Object> dict = new HashMap<String, Object>();
		dict.put("address", getAddress());
		dict.put("dob", getDob());
		dict.put("firstName", getFirstName());
		dict.put("lastName", getLastName());
		Map<String, String> verification = new HashMap<String, String>();
		verification.put("document", getVerificationDocumentId());
		dict.put("verification", verification);
		return dict;
	}
}
