/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

package com.wsl.modules.stripe.client;

import java.util.HashMap;
import java.util.Map;

import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ReconnectOn;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.wsl.modules.stripe.complextypes.TimeRange;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Coupon-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeCouponClient {
	/**
     * Create a Coupon
     *
     * @param id Unique string of your choice that will be used to identify this coupon when applying it a customer. This is often a specific code you’ll give to your customer to use when signing up (e.g. FALL25OFF). If you don’t want to specify a particular code, you can leave the ID blank and we’ll generate a random code for you.
     * @param duration Specifies how long the discount will be in effect. Can be forever, once, or repeating.
     * @param amountOff A positive integer representing the amount to subtract from an invoice total (required if percent_off is not passed)
     * @param currency Currency of the amount_off parameter (required if amount_off is passed)
     * @param durationInMonths required only if duration is repeating If duration is repeating, a positive integer that specifies the number of months the discount will be in effect
     * @param maxRedemptions A positive integer specifying the number of times the coupon can be redeemed before it’s no longer valid. For example, you might have a 50% off coupon that the first 20 readers of your blog can use.
     * @param percentOff A positive integer between 1 and 100 that represents the discount the coupon will apply (required if amount_off is not passed)
     * @param redeemBy Unix timestamp specifying the last time at which the coupon can be redeemed. After the redeem_by date, the coupon can no longer be applied to new customers.
     * @param metadata A set of key/value pairs that you can attach to a coupon object. It can be useful for storing additional information about the coupon in a structured format. 
     * @return Coupon created
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Coupon createCoupon(String id, String duration, int amountOff, String currency, int durationInMonths, int maxRedemptions, int percentOff, String redeemBy, Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", id);
    	params.put("duration", duration);
    	params.put("amount_off", amountOff);
    	params.put("currency", currency);
    	params.put("duration_in_months", durationInMonths);
    	params.put("max_redemptions", maxRedemptions);
    	params.put("metadata", metadata);
    	params.put("percent_off", percentOff);
    	params.put("redeem_by", redeemBy);
		params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
			return Coupon.create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create the coupon", e);
		}
    }
    
    /**
     * Retrieve a Coupon
     *
     * @param id The ID of the desired coupon.
     * @return Coupon retrieved
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Coupon retrieveCoupon(String id) 
    		throws StripeConnectorException {
    	try {
			return Coupon.retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the coupon", e);
		}
    }
    
    /**
     * Update a Coupon
     * Updates the metadata of a coupon. Other coupon details (currency, duration, amount_off) are, by design, not editable.
     *
     * @param id The ID of the coupon to be updated.
     * @param metadata A set of key/value pairs that you can attach to a coupon object. It can be useful for storing additional information about the coupon in a structured format.
     * @return Coupon updated
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Coupon updateCoupon(String id, Map<String, Object> metadata) 
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("metadata", metadata);
    	try {
    		Coupon coupon = Coupon.retrieve(id);
			return coupon.update(params);			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not update the coupon", e);
		}
    }
    
    /**
     * Delete a Coupon
     *
     * @param id The ID of the coupon to be deleted.     
     * @return An object with the deleted coupon's ID and a deleted flag upon success. Otherwise, this call throws an error, such as if the coupon has already been deleted.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Object deleteCoupon(String id) 
    		throws StripeConnectorException {
    	try {
    		Coupon coupon = Coupon.retrieve(id);
			return coupon.delete();			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not delete the coupon", e);
		}
    }
    
    /**
     * List all Coupons
     *
     *  @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp,...
     * @param created ... or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.     
     * @return A Map with a data property that contains an array of up to limit coupons, starting after coupon starting_after. Each entry in the array is a separate coupon object. If no more coupons are available, the resulting array will be empty. This request should never throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public CouponCollection listAllCoupons(String createdTimestamp, TimeRange created, String endingBefore, int limit, String startingAfter) 
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (limit != 0){
    		params.put("limit", limit);
    	}
    	if (createdTimestamp != null && !createdTimestamp.isEmpty()){
    		params.put("created", createdTimestamp);
    	} else if (created != null){
    		params.put("created", created.toDict());
    	}
    	params.put("ending_before", endingBefore);
    	params.put("starting_after", startingAfter);
    	params = StripeClientUtils.removeOptionals(params);
    	try {
    		return Coupon.all(params);			
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list coupons", e);
		}
    }
}
