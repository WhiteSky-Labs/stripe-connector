
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertNull;
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

public class DeletePlanTestCases
    extends StripeTestParent
{


	private String planId = "deletePlanTest";

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createPlanTestData");
    	upsertOnTestRunMessage("id", this.planId);       
        Object result = runFlowAndGetPayload("create-plan");
        initializeTestRunMessage("deletePlanTestData");
        upsertOnTestRunMessage("id", planId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	try {
    		initializeTestRunMessage("retrievePlanTestData");
    		upsertOnTestRunMessage("id", this.planId);
    		Object result = runFlowAndGetPayload("retrieve-plan");
            Plan plan = (Plan) result;
            if (plan != null) {
            	initializeTestRunMessage("deletePlanTestData");
            	upsertOnTestRunMessage("id", this.planId);
            	runFlowAndGetPayload("delete-plan");
            }
    	} catch (MessagingException e){
    		// this is expected, if the plan doesn't exist.
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeletePlan()
        throws Exception
    {
        Object result = runFlowAndGetPayload("delete-plan");
        /*
         * Result will be of the format 
         * { "deleted": true, "id": "pln_5yv1Swh5U1jp4P" }
         */
        assertTrue(result.toString().contains("\"deleted\": true"));
        assertTrue(result.toString().contains(this.planId));
        // Confirm the plan doesn't exist
        initializeTestRunMessage("retrievePlanTestData");
        upsertOnTestRunMessage("id", this.planId);
        
        try {
    		runFlowAndGetPayload("retrieve-plan");
    		fail("Retrieving a plan that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the plan"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeletePlanThatDoesntExist()
        throws Exception
    {
        upsertOnTestRunMessage("id", "InvalidID");
    	try {
    		runFlowAndGetPayload("delete-plan");
    		fail("Deleting a plan that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the plan"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
