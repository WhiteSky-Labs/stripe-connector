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
 * Wrapper for List all Coupons Parameters
 * @author WhiteSky Labs
 *
 */
public class ListAllCouponsParameters {	
	private String createdTimestamp;
	private TimeRange created;
	private String endingBefore;
	private int limit;
	private String startingAfter;
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
	
}
