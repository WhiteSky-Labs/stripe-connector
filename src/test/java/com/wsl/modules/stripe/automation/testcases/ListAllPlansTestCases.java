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
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;

import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.CreateSubscriptionParameters;
import com.wsl.modules.stripe.complextypes.ListAllPlansParameters;

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
        runFlowAndGetPayload("create-plan");
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
    	Map<String, Object> expectedBean = getBeanFromContext("listAllPlansTestData");
        ListAllPlansParameters params = (ListAllPlansParameters) expectedBean.get("listAllPlansParameters");
        params.setLimit(1);
        upsertOnTestRunMessage("listAllPlansParameters", params);
        
        Object result = runFlowAndGetPayload("list-all-plans");
        assertNotNull(result);
        PlanCollection coll = (PlanCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListPlansWithCreated()
        throws Exception
    {
    	initializeTestRunMessage("listAllPlansCreatedTestData");
    	Map<String, Object> expectedBean = getBeanFromContext("listAllPlansCreatedTestData");
        ListAllPlansParameters params = (ListAllPlansParameters) expectedBean.get("listAllPlansParameters");
        params.setLimit(1);
        upsertOnTestRunMessage("listAllPlansParameters", params);
        
        Object result = runFlowAndGetPayload("list-all-plans");
        assertNotNull(result);
        PlanCollection coll = (PlanCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }
}
