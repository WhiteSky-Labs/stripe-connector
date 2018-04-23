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

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Event;
import com.stripe.model.EventCollection;
import com.wsl.modules.stripe.complextypes.TimeRange;
import com.wsl.modules.stripe.exceptions.StripeConnectorException;
import com.wsl.modules.stripe.utils.StripeClientUtils;

/**
 * Encapsulates Event-related Stripe API calls.
 * @author WhiteSky Labs
 *
 */
public class StripeEventClient {
    /**
     * Retrieve a Event
     * Retrieves the Event with the given ID
	 * 
     * @param id	The ID of the desired event
     * @return Returns a event if a valid ID was provided. Throws an error otherwise.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public Event retrieveEvent(String id)
    		throws StripeConnectorException {
    	try {    		
    		return Event.retrieve(id);    		
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not retrieve the Event", e);
		}
    }
    
    /**
     * List All Events
     * List events, going back up to 30 days.
	 * 
     * @param createdTimestamp A filter on the list based on the object created field. The value can be a string with an integer Unix timestamp
     * @param created or it can be a dictionary with the following options: gt, gte, lt, lte
     * @param endingBefore A cursor for use in pagination. ending_before is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, starting with obj_bar, your subsequent call can include ending_before=obj_bar in order to fetch the previous page of the list.
     * @param limit A limit on the number of objects to be returned. Limit can range between 1 and 100 items.
     * @param startingAfter A cursor for use in pagination. starting_after is an object ID that defines your place in the list. For instance, if you make a list request and receive 100 objects, ending with obj_foo, your subsequent call can include starting_after=obj_foo in order to fetch the next page of the list.
     * @param type A string containing a specific event name, or group of events using * as a wildcard. The list will be filtered to include only events with a matching event property
     * @return A Map with a data property that contains an array of up to limit events, starting after event starting_after, sorted in reverse chronological order. Each entry in the array is a separate event object. If no more events are available, the resulting array will be empty. This request should never throw an error.
     * @throws StripeConnectorException when there is a problem with the Connector
     */
    public EventCollection listAllEvents(String createdTimestamp, TimeRange created, String endingBefore, int limit, String startingAfter, String type)
    		throws StripeConnectorException {
    	Map<String, Object> params = new HashMap<String, Object>();
    	if (createdTimestamp != null && !createdTimestamp.isEmpty()){
    		params.put("created", createdTimestamp);    		
    	} else if (created != null){
    		params.put("created", created.toDict());    	
    	}
    	params.put("ending_before", endingBefore);
    	params.put("limit", limit);
    	params.put("startingAfter", startingAfter);
    	params.put("type", type);
    	params = StripeClientUtils.removeOptionalsAndZeroes(params);
    	try {
    		return Event.all(params);
		} catch (AuthenticationException | InvalidRequestException
				| APIConnectionException | CardException | APIException e) {
			throw new StripeConnectorException("Could not list Events", e);
		}
    }
}
