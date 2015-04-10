/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.stripe.model.Customer;
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

public class RetrievePlanTestCases
    extends StripeTestParent
{


	private String planId = "retrieveTestPlan";

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createPlanTestData");
    	upsertOnTestRunMessage("id", this.planId);
    	Object result = runFlowAndGetPayload("create-plan");
        initializeTestRunMessage("retrievePlanTestData");
        upsertOnTestRunMessage("id", this.planId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	initializeTestRunMessage("deletePlanTestData");
        upsertOnTestRunMessage("id", this.planId);
        runFlowAndGetPayload("delete-plan");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrievePlan()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-plan");
        assertNotNull(result);
        Plan plan = (Plan)result;
        assertEquals(this.planId, plan.getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentPlan()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-plan");
            fail("Getting a plan that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the plan"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
