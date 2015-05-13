/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

package com.wsl.modules.stripe.complextypes;

/**
 * Wrapper for List All Balance History Parameters
 * @author WhiteSky Labs
 *
 */
public class ListAllBalanceHistoryParameters {
	private String availableOnTimestamp;
	private TimeRange availableOn;
	private String createdTimestamp;
	private TimeRange created;
	private String currency;
	private String endingBefore;
	private int limit;
	private String sourceId;
	private String startingAfter;
	private String transfer;
	private String type;
	/**
	 * @return the availableOnTimestamp
	 */
	public String getAvailableOnTimestamp() {
		return availableOnTimestamp;
	}
	/**
	 * @param availableOnTimestamp the availableOnTimestamp to set
	 */
	public void setAvailableOnTimestamp(String availableOnTimestamp) {
		this.availableOnTimestamp = availableOnTimestamp;
	}
	/**
	 * @return the availableOn
	 */
	public TimeRange getAvailableOn() {
		return availableOn;
	}
	/**
	 * @param availableOn the availableOn to set
	 */
	public void setAvailableOn(TimeRange availableOn) {
		this.availableOn = availableOn;
	}
	/**
	 * @return the createdTimestamp
	 */
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}
	/**
	 * @param createdTimestamp the createdTimestamp to set
	 */
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	/**
	 * @return the created
	 */
	public TimeRange getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(TimeRange created) {
		this.created = created;
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
	 * @return the endingBefore
	 */
	public String getEndingBefore() {
		return endingBefore;
	}
	/**
	 * @param endingBefore the endingBefore to set
	 */
	public void setEndingBefore(String endingBefore) {
		this.endingBefore = endingBefore;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return the sourceId
	 */
	public String getSourceId() {
		return sourceId;
	}
	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	/**
	 * @return the startingAfter
	 */
	public String getStartingAfter() {
		return startingAfter;
	}
	/**
	 * @param startingAfter the startingAfter to set
	 */
	public void setStartingAfter(String startingAfter) {
		this.startingAfter = startingAfter;
	}
	/**
	 * @return the transfer
	 */
	public String getTransfer() {
		return transfer;
	}
	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}
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
	
}
