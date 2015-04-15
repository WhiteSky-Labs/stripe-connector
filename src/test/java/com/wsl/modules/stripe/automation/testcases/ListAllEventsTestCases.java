/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import com.stripe.model.Event;
import com.stripe.model.EventCollection;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class ListAllEventsTestCases
    extends StripeTestParent
{

	private String planId;
	
	
    @Before
    public void setup()
        throws Exception
    {
        initializeTestRunMessage("createPlanTestData");
        Plan plan = (Plan) runFlowAndGetPayload("create-plan");
        this.planId = plan.getId();
        initializeTestRunMessage("listAllEventsTestData");
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
    public void testListAllEvents()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-events");
        EventCollection coll = (EventCollection) result;
        Event event = coll.getData().get(0);
        assertEquals("plan.created", event.getType());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListAllEventsWithError()
        throws Exception
    {
    	try {
    		upsertOnTestRunMessage("createdTimestamp", "Invalid");
        	Object result = runFlowAndGetPayload("list-all-events");
            fail("Should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not list Events"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }
}
