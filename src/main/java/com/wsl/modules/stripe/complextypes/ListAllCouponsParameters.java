/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
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
