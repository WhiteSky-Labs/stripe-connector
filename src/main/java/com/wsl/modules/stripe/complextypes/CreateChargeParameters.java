/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

package com.wsl.modules.stripe.complextypes;

import java.util.Map;

/**
 * Wraps the Create Charge parameters
 * @author WhiteSky Labs
 *
 */
public class CreateChargeParameters {
	private int amount;
	private String currency;
	private String customerId;
	private Source source;
	private String description;
	private Map<String, Object> metadata;
	private boolean capture;
	private String statementDescriptor;
	private String destination;
	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}
	private String receiptEmail;
	private int applicationFee;
	private Map<String, String> shipping;
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
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
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(Source source) {
		this.source = source;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	/**
	 * @return the capture
	 */
	public boolean isCapture() {
		return capture;
	}
	/**
	 * @param capture the capture to set
	 */
	public void setCapture(boolean capture) {
		this.capture = capture;
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
	 * @return the receiptEmail
	 */
	public String getReceiptEmail() {
		return receiptEmail;
	}
	/**
	 * @param receiptEmail the receiptEmail to set
	 */
	public void setReceiptEmail(String receiptEmail) {
		this.receiptEmail = receiptEmail;
	}
	/**
	 * @return the applicationFee
	 */
	public int getApplicationFee() {
		return applicationFee;
	}
	/**
	 * @param applicationFee the applicationFee to set
	 */
	public void setApplicationFee(int applicationFee) {
		this.applicationFee = applicationFee;
	}
	/**
	 * @return the shipping
	 */
	public Map<String, String> getShipping() {
		return shipping;
	}
	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(Map<String, String> shipping) {
		this.shipping = shipping;
	}
	
}
