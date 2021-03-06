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
 * Represents a Transfer Schedule in the Stripe Input API
 * @author WhiteSky Labs
 *
 */
public class TransferSchedule implements java.io.Serializable{
	private String delayDays;
	private String interval;
	private String monthlyAnchor;
	private String weeklyAnchor;
	/**
	 * @return the delayDays
	 */
	public String getDelayDays() {
		return delayDays;
	}
	/**
	 * @param delayDays the delayDays to set
	 */
	public void setDelayDays(String delayDays) {
		this.delayDays = delayDays;
	}
	/**
	 * @return the interval
	 */
	public String getInterval() {
		return interval;
	}
	/**
	 * @param interval the interval to set
	 */
	public void setInterval(String interval) {
		this.interval = interval;
	}
	/**
	 * @return the monthlyAnchor
	 */
	public String getMonthlyAnchor() {
		return monthlyAnchor;
	}
	/**
	 * @param monthlyAnchor the monthlyAnchor to set
	 */
	public void setMonthlyAnchor(String monthlyAnchor) {
		this.monthlyAnchor = monthlyAnchor;
	}
	/**
	 * @return the weeklyAnchor
	 */
	public String getWeeklyAnchor() {
		return weeklyAnchor;
	}
	/**
	 * @param weeklyAnchor the weeklyAnchor to set
	 */
	public void setWeeklyAnchor(String weeklyAnchor) {
		this.weeklyAnchor = weeklyAnchor;
	}
	/**
	 * Turns the object into a Stripe API-compliant dictionary with the correct values.
	 * 
	 * @return A map that represents the JSON dictionary.
	 */
	public Map<String, Object> toDictionary(){
		Map<String, Object> dict = new HashMap<String, Object>();
		dict.put("delay_days", getDelayDays());
		dict.put("interval", getInterval());
		dict.put("monthly_anchor", getMonthlyAnchor());
		dict.put("weekly_anchor", getWeeklyAnchor());
		return StripeClientUtils.removeOptionals(dict);		
	}

}
