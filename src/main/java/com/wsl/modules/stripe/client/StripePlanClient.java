/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
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
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.wsl.modules.stripe.complextypes.TimeRange;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Plan-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripePlanClient {
	/**
     * Create a Plan
     *
     * @param id Unique string of your choice that will be used to identify this plan when subscribing a customer. This could be an identifier like "gold" or a primary key from your own database.
     * @param amount A positive integer in cents (or 0 for a free plan) representing how much to charge (on a recurring basis).
     * @param currency 3-letter ISO code for currency.
     * @param interval Specifies billing frequency. Either day, week, month or year.
     * @param intervalCount The number of intervals between each subscription billing. For example, interval=month and interval_count=3 bills every 3 months. Maximum of one year interval allowed (1 year, 12 months, or 52 weeks).
     * @param planName Name of the plan, to be displayed on invoices and in the web interface.
     * @param trialPeriodDays Specifies a trial period in (an integer number of) days. If you include a trial period, the customer won't be billed for the first time until the trial period ends. If the customer cancels before the trial period is over, she'll never be billed at all.
     * @param statementDescriptor An arbitrary string to be displayed on your customer's credit card statement. This may be up to22 characters. 
     * @param metadata A set of key/value pairs that you can attach to a plan object. It can be useful for storing additional information about the plan in a structured format.
     * @return Plan the created Plan
     * @throws StripeConnectorException when there is an issue creating a Plan 
     */
    public Plan createPlan(String id, int amount, String currency, String interval, int intervalCount, String planName, int trialPeriodDays, String statementDescriptor, Map<String, Object> metadata) throws StripeConnectorException{
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (trialPeriodDays > 0){
    		params.put("trial_period_days", trialPeriodDays);
    	}
    	params.put("id", id);
    	params.put("amount", amount);
    	params.put("currency", currency);
    	params.put("interval", interval);
    	params.put("interval_count", intervalCount);
    	params.put("name", planName);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("metadata", metadata);
    	params = StripeClientUtils.removeOptionals(params);
    	try {
			return Plan.create(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a plan", e);
		}
    }
    
    /**
     * Retrieve a Plan
     *
     * @param id The id of the plan to retrieve
     * @return Plan the retrieved Plan
     * @throws StripeConnectorException when there is an issue retrieving a Plan 
     */
    public Plan retrievePlan(String id) throws StripeConnectorException{
    	try {
			return Plan.retrieve(id);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the plan", e);
		}
    }
    
    /**
     * Update a Plan
     *
     * @param id of the plan to update
     * @param planName Name of the plan, to be displayed on invoices and in the web interface.
     * @param statementDescriptor An arbitrary string to be displayed on your customer's credit card statement. This may be up to22 characters. 
     * @param metadata A set of key/value pairs that you can attach to a plan object. It can be useful for storing additional information about the plan in a structured format.
     * @return Plan the updated Plan
     * @throws StripeConnectorException when there is an issue updating a Plan 
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public Plan updatePlan(String id, String planName, String statementDescriptor, Map<String, Object> metadata) throws StripeConnectorException{
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("name", planName);
    	params.put("statement_descriptor", statementDescriptor);
    	params.put("metadata", metadata);
    	params = StripeClientUtils.removeOptionals(params);
    	try {
			Plan plan = Plan.retrieve(id);
			return plan.update(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not create a plan", e);
		}
    }
    
    /**
     * Delete a Plan
     *
     * @param id The id of the plan to delete
     * @return Object JSON containing success or failure
     * @throws StripeConnectorException when there is an issue deleting a Plan 
     */
    public Object deletePlan(String id) throws StripeConnectorException{
    	try {
    		Plan plan = Plan.retrieve(id);
			return plan.delete();
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the plan", e);
		}
    }
    
    /**
     * List All Plans
     *
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp.
     * @param created A filter on the list based on the object created field. The value can be a dictionary containing gt, gte, lt and/or lte values. You cannot supply a created value and this dictionary at the same time.
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @return PlanCollection the plans that matched the criteria
     * @throws StripeConnectorException when there is an issue listing plans
     */
    @Processor
    @ReconnectOn(exceptions = { Exception.class })
    public PlanCollection listAllPlans(String createdTimestamp, TimeRange created, String endingBefore, int limit, String startingAfter) throws StripeConnectorException{
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (limit > 0){
    		params.put("limit", limit);
    	}
    	if (createdTimestamp != null){
    		params.put("created", createdTimestamp);
    	} else if (created != null) {
    		params.put("created", created.toDict());
    	}
    	params.put("ending_before", endingBefore);
    	params.put("starting_after", startingAfter);
    	params = StripeClientUtils.removeOptionals(params);
    	try {
    		return Plan.all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list plans", e);
		}
    }
}
