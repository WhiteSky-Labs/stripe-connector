
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class CreatePlanTestCases
    extends StripeTestParent
{


private List<String> planIds = new ArrayList<String>();
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createPlanTestData");
    	
    }

    @After
    public void tearDown()
        throws Exception
    {
        initializeTestRunMessage("deletePlanTestData");
        for (String planId : planIds){
        	upsertOnTestRunMessage("id", planId);
        	runFlowAndGetPayload("delete-plan");
        }
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreatePlan()
        throws Exception
    {
    	Object result = runFlowAndGetPayload("create-plan");
        assertNotNull(result);
        Plan plan = (Plan) result;
        assertNotNull(plan.getId());
        assertTrue(plan.getId().length() > 0);
        this.planIds.add(plan.getId());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreatePlanWithValidOptionalParams()
        throws Exception
    {
    	Object result = runFlowAndGetPayload("create-plan", "createPlanWithValidParamsTestData");
    	Map<String, Object> expectedBean = getBeanFromContext("createPlanWithValidParamsTestData");
    	assertNotNull(result);
        Plan plan= (Plan) result;
        assertNotNull(plan.getId());
        assertTrue(plan.getId().length() > 0);
        this.planIds.add(plan.getId());
        assertEquals(expectedBean.get("trialPeriodDays"), plan.getTrialPeriodDays().toString());
        assertEquals(expectedBean.get("metadata"), plan.getMetadata());
    }


}