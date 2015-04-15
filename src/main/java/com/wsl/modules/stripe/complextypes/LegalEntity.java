/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe.complextypes;

import java.util.*;

import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Represents a Legal Entity in the Stripe API
 * @author WhiteSky Labs
 *
 */
public class LegalEntity {
	private String type;
	private Address address;
	private String businessName;
	private String businessTaxId;
	private String businessVatId;
	private DateOfBirth dob;
	private String firstName;
	private String lastName;
	private Address personalAddress;
	private String personalIdNumber;
	private String ssnLast4;
	private String verificationDocumentId;
	private List<Owner> additionalOwners;	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
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
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	/**
	 * @return the businessTaxId
	 */
	public String getBusinessTaxId() {
		return businessTaxId;
	}
	/**
	 * @param businessTaxId the businessTaxId to set
	 */
	public void setBusinessTaxId(String businessTaxId) {
		this.businessTaxId = businessTaxId;
	}
	/**
	 * @return the businessVatId
	 */
	public String getBusinessVatId() {
		return businessVatId;
	}
	/**
	 * @param businessVatId the businessVatId to set
	 */
	public void setBusinessVatId(String businessVatId) {
		this.businessVatId = businessVatId;
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
	 * @return the personalAddress
	 */
	public Address getPersonalAddress() {
		return personalAddress;
	}
	/**
	 * @param personalAddress the personalAddress to set
	 */
	public void setPersonalAddress(Address personalAddress) {
		this.personalAddress = personalAddress;
	}
	/**
	 * @return the personalIdNumber
	 */
	public String getPersonalIdNumber() {
		return personalIdNumber;
	}
	/**
	 * @param personalIdNumber the personalIdNumber to set
	 */
	public void setPersonalIdNumber(String personalIdNumber) {
		this.personalIdNumber = personalIdNumber;
	}
	/**
	 * @return the ssnLast4
	 */
	public String getSsnLast4() {
		return ssnLast4;
	}
	/**
	 * @param ssnLast4 the ssnLast4 to set
	 */
	public void setSsnLast4(String ssnLast4) {
		this.ssnLast4 = ssnLast4;
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
	 * @return the additionalOwners
	 */
	public List<Owner> getAdditionalOwners() {
		return this.additionalOwners;
	}
	/**
	 * @param additionalOwners the additionalOwners to set
	 */
	public void setAdditionalOwners(List<Owner> additionalOwners) {
		this.additionalOwners = additionalOwners;
	}
	/**
	 * Turns the object into a Stripe API-compliant dictionary with the correct values.
	 * 
	 * @return A map that represents the JSON dictionary.
	 */
	public Map<String, Object> toDictionary(){
		Map<String, Object> dict = new HashMap<String, Object>();
		dict.put("type", getType());
		if (getAddress() != null){
			dict.put("address", getAddress().toDictionary());
		}
		dict.put("business_name", getBusinessName());
		dict.put("business_tax_id", getBusinessTaxId());
		dict.put("business_vat_id", getBusinessVatId());
		if (getDob() != null){
			dict.put("dob", getDob().toDictionary());
		}
		dict.put("first_name", getFirstName());
		dict.put("last_name", getLastName());
		if (getAddress() != null){
			dict.put("personal_address", getAddress().toDictionary());
		}
		dict.put("personal_id_number", getPersonalIdNumber());
		dict.put("ssn_last_4", getSsnLast4());
		if (getVerificationDocumentId() != null){
			Map<String, String> verification = new HashMap<String, String>();
			verification.put("document", getVerificationDocumentId());
			dict.put("verification", verification);
		}
		if (getAdditionalOwners() != null){
			List<Map<String, Object>> objectList = new ArrayList<Map<String,Object>>();		
			for (Owner owner : getAdditionalOwners()){
				objectList.add(owner.toDictionary());
			}
			dict.put("additional_owners", objectList);		
		}
		return StripeClientUtils.removeOptionalsAndZeroes(dict);
	}
}
