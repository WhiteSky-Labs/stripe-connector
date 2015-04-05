
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ListAllPlansTestCases
    extends StripeTestParent
{

	private String planId = "listPlansTestPlan";

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createPlanTestData");
    	upsertOnTestRunMessage("id", planId);
        Object result = runFlowAndGetPayload("create-plan");
        initializeTestRunMessage("listAllPlansTestData");
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
    public void testListAllPlans()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-plans");
        assertNotNull(result);
        PlanCollection coll = (PlanCollection)result;
        Iterator<Plan> it = coll.getData().iterator();
        boolean foundPlan = false;
        while (it.hasNext()) {
            Plan plan = it.next();
            if (plan.getId().equals(this.planId)){
            	foundPlan = true;
            }
        }
        assertTrue(foundPlan);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListPlansWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-plans");
        assertNotNull(result);
        PlanCollection coll = (PlanCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }

}
