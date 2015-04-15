/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.stripe.model.Event;
import com.stripe.model.EventCollection;
import com.stripe.model.Plan;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class RetrieveEventTestCases
    extends StripeTestParent
{

	private String planId;
	private String eventId;
	
    @Before
    public void setup()
        throws Exception
    {
        initializeTestRunMessage("createPlanTestData");
        Plan plan = (Plan) runFlowAndGetPayload("create-plan");
        this.planId = plan.getId();
        initializeTestRunMessage("listAllEventsTestData");
        Object result = runFlowAndGetPayload("list-all-events");
        EventCollection coll = (EventCollection) result;
        Event event = coll.getData().get(0);
        initializeTestRunMessage("retrieveEventTestData");
        upsertOnTestRunMessage("id", event.getId());
        this.eventId = event.getId();
    }

    @After
    public void tearDown()
        throws Exception
    {
        initializeTestRunMessage("deletePlanTestData");
        upsertOnTestRunMessage("id", planId);
        runFlowAndGetPayload("delete-plan");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveEvent()
        throws Exception
    {
        Event event = (Event) runFlowAndGetPayload("retrieve-event");
        assertEquals(this.eventId, event.getId());
        assertEquals("plan.created", event.getType());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentEvent()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            runFlowAndGetPayload("retrieve-event");
            fail("Getting an event that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Event"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}        
    }


}
