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
import static org.junit.Assert.assertNotNull;

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

public class UpdatePlanTestCases
    extends StripeTestParent
{

	private String planId = "updateTestPlan";

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createPlanTestData");
    	upsertOnTestRunMessage("id", planId);
    	Object result = runFlowAndGetPayload("create-plan");
        initializeTestRunMessage("updatePlanTestData");
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
    public void testUpdatePlan()
        throws Exception
    {
    	Map<String, Object> expectedBean = getBeanFromContext("updatePlanTestData");
    	upsertOnTestRunMessage("id", this.planId);
        Object result = runFlowAndGetPayload("update-plan");
        assertNotNull(result);
        Plan plan = (Plan) result;
        assertEquals(expectedBean.get("planName"), plan.getName());
        assertEquals(expectedBean.get("statementDescriptor"), plan.getStatementDescriptor());
        
    }
}
