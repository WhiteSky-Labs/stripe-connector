/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe.complextypes;

import java.util.Map;

/**
 * Create Account Parameter Wrapper
 * @author WhiteSky Labs
 *
 */
public class CreateAccountParameters {
	private boolean managed;
	private String country;
	private String email;
	private String businessName;
	private String businessUrl;
	private String supportPhone;
	private BankAccount bankAccount;
	private boolean debitNegativeBalances;
	private String defaultCurrency;
	private LegalEntity legalEntity;
	private String productDescription;
	private String statementDescriptor;
	private Acceptance tosAcceptance;
	private TransferSchedule transferSchedule;
	private Map<String, Object> metadata;
	/**
	 * @return the managed
	 */
	public boolean isManaged() {
		return managed;
	}
	/**
	 * @param managed the managed to set
	 */
	public void setManaged(boolean managed) {
		this.managed = managed;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the businessUrl
	 */
	public String getBusinessUrl() {
		return businessUrl;
	}
	/**
	 * @param businessUrl the businessUrl to set
	 */
	public void setBusinessUrl(String businessUrl) {
		this.businessUrl = businessUrl;
	}
	/**
	 * @return the supportPhone
	 */
	public String getSupportPhone() {
		return supportPhone;
	}
	/**
	 * @param supportPhone the supportPhone to set
	 */
	public void setSupportPhone(String supportPhone) {
		this.supportPhone = supportPhone;
	}
	/**
	 * @return the bankAccount
	 */
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the debitNegativeBalances
	 */
	public boolean isDebitNegativeBalances() {
		return debitNegativeBalances;
	}
	/**
	 * @param debitNegativeBalances the debitNegativeBalances to set
	 */
	public void setDebitNegativeBalances(boolean debitNegativeBalances) {
		this.debitNegativeBalances = debitNegativeBalances;
	}
	/**
	 * @return the defaultCurrency
	 */
	public String getDefaultCurrency() {
		return defaultCurrency;
	}
	/**
	 * @param defaultCurrency the defaultCurrency to set
	 */
	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}
	/**
	 * @return the legalEntity
	 */
	public LegalEntity getLegalEntity() {
		return legalEntity;
	}
	/**
	 * @param legalEntity the legalEntity to set
	 */
	public void setLegalEntity(LegalEntity legalEntity) {
		this.legalEntity = legalEntity;
	}
	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}
	/**
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	/**
	 * @return the statementDescriptor
	 */
	public String getStatementDescriptor() {
		return statementDescriptor;
	}
	/**
	 * @param statementDescriptor the statementDescriptor to set
	 */
	public void setStatementDescriptor(String statementDescriptor) {
		this.statementDescriptor = statementDescriptor;
	}
	/**
	 * @return the tosAcceptance
	 */
	public Acceptance getTosAcceptance() {
		return tosAcceptance;
	}
	/**
	 * @param tosAcceptance the tosAcceptance to set
	 */
	public void setTosAcceptance(Acceptance tosAcceptance) {
		this.tosAcceptance = tosAcceptance;
	}
	/**
	 * @return the transferSchedule
	 */
	public TransferSchedule getTransferSchedule() {
		return transferSchedule;
	}
	/**
	 * @param transferSchedule the transferSchedule to set
	 */
	public void setTransferSchedule(TransferSchedule transferSchedule) {
		this.transferSchedule = transferSchedule;
	}
	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}
	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	
	
}
