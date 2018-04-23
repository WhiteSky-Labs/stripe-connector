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
 * Wrapper for Update Subscription Parameters
 * @author WhiteSky Labs
 *
 */
public class UpdateSubscriptionParameters {
	private String customerId;
	private String subscriptionId;
	private String plan;
	private String coupon;
	private boolean prorate;
	private String trialEnd;
	private String sourceToken;
	private Source source;
	private int quantity;
	private double applicationFeePercent;
	private double taxPercent; 
	private Map<String, Object> metadata;
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
	 * @return the subscriptionId
	 */
	public String getSubscriptionId() {
		return subscriptionId;
	}
	/**
	 * @param subscriptionId the subscriptionId to set
	 */
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	/**
	 * @return the plan
	 */
	public String getPlan() {
		return plan;
	}
	/**
	 * @param plan the plan to set
	 */
	public void setPlan(String plan) {
		this.plan = plan;
	}
	/**
	 * @return the coupon
	 */
	public String getCoupon() {
		return coupon;
	}
	/**
	 * @param coupon the coupon to set
	 */
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	/**
	 * @return the prorate
	 */
	public boolean isProrate() {
		return prorate;
	}
	/**
	 * @param prorate the prorate to set
	 */
	public void setProrate(boolean prorate) {
		this.prorate = prorate;
	}
	/**
	 * @return the trialEnd
	 */
	public String getTrialEnd() {
		return trialEnd;
	}
	/**
	 * @param trialEnd the trialEnd to set
	 */
	public void setTrialEnd(String trialEnd) {
		this.trialEnd = trialEnd;
	}
	/**
	 * @return the sourceToken
	 */
	public String getSourceToken() {
		return sourceToken;
	}
	/**
	 * @param sourceToken the sourceToken to set
	 */
	public void setSourceToken(String sourceToken) {
		this.sourceToken = sourceToken;
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
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the applicationFeePercent
	 */
	public double getApplicationFeePercent() {
		return applicationFeePercent;
	}
	/**
	 * @param applicationFeePercent the applicationFeePercent to set
	 */
	public void setApplicationFeePercent(double applicationFeePercent) {
		this.applicationFeePercent = applicationFeePercent;
	}
	/**
	 * @return the taxPercent
	 */
	public double getTaxPercent() {
		return taxPercent;
	}
	/**
	 * @param taxPercent the taxPercent to set
	 */
	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
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
