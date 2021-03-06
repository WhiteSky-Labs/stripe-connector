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
 * Implements a Time Range wrapper for the Stripe API
 * @author WhiteSky Labs
 *
 */
public class TimeRange implements java.io.Serializable{
	private String greaterThan;
	private String lessThan;
	private String lessThanOrEqualTo;
	private String greaterThanOrEqualTo;
	/**
	 * @return the greaterThan
	 */
	public String getGreaterThan() {
		return greaterThan;
	}
	/**
	 * @param greaterThan the greaterThan to set
	 */
	public void setGreaterThan(String greaterThan) {
		this.greaterThan = greaterThan;
	}
	/**
	 * @return the lessThan
	 */
	public String getLessThan() {
		return lessThan;
	}
	/**
	 * @param lessThan the lessThan to set
	 */
	public void setLessThan(String lessThan) {
		this.lessThan = lessThan;
	}
	/**
	 * @return the lessThanOrEqualTo
	 */
	public String getLessThanOrEqualTo() {
		return lessThanOrEqualTo;
	}
	/**
	 * @param lessThanOrEqualTo the lessThanOrEqualTo to set
	 */
	public void setLessThanOrEqualTo(String lessThanOrEqualTo) {
		this.lessThanOrEqualTo = lessThanOrEqualTo;
	}
	/**
	 * @return the greaterThanOrEqualTo
	 */
	public String getGreaterThanOrEqualTo() {
		return greaterThanOrEqualTo;
	}
	/**
	 * @param greaterThanOrEqualTo the greaterThanOrEqualTo to set
	 */
	public void setGreaterThanOrEqualTo(String greaterThanOrEqualTo) {
		this.greaterThanOrEqualTo = greaterThanOrEqualTo;
	}
	
	/**
	 * Turns the object into a dictionary for Stripe
	 * @return A dictionary in Stripe format
	 */
	public Map<String, Object> toDict(){
		Map<String, Object> dict = new HashMap<String, Object>();
		dict.put("gt", getGreaterThan());
		dict.put("gte", getGreaterThanOrEqualTo());
		dict.put("lt", getLessThan());
		dict.put("lte", getLessThanOrEqualTo());
		return StripeClientUtils.removeOptionals(dict);
	}
}
