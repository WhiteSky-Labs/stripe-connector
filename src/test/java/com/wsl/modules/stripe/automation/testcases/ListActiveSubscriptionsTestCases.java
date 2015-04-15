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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class ListActiveSubscriptionsTestCases
    extends StripeTestParent
{

	private String customerId;
	private String subscriptionId;	
	private String planId;
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createCustomerTestData");
    	Object result = runFlowAndGetPayload("create-customer");
        Customer customer = (Customer) result;
        this.customerId = customer.getId();
        initializeTestRunMessage("createPlanTestData");
        result = runFlowAndGetPayload("create-plan");
        Plan plan = (Plan)result;
        this.planId = plan.getId();
        initializeTestRunMessage("createSubscriptionTestData");
        upsertOnTestRunMessage("customerId", customerId);
        upsertOnTestRunMessage("plan", planId);
        result = runFlowAndGetPayload("create-subscription");
        Subscription sub = (Subscription) result;
		this.subscriptionId = sub.getId();
		initializeTestRunMessage("listActiveSubscriptionsTestData");
		upsertOnTestRunMessage("customerId", this.customerId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	initializeTestRunMessage("cancelSubscriptionTestData");
    	upsertOnTestRunMessage("customerId", customerId);
    	upsertOnTestRunMessage("subscriptionId", subscriptionId);
    	runFlowAndGetPayload("cancel-subscription");
    	initializeTestRunMessage("deleteCustomerTestData");
        upsertOnTestRunMessage("id", customerId);
        runFlowAndGetPayload("delete-customer");
        initializeTestRunMessage("deletePlanTestData");
        upsertOnTestRunMessage("id", this.planId);
        runFlowAndGetPayload("delete-plan");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListActiveSubscriptions()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("list-active-subscriptions");
        assertNotNull(result);
        CustomerSubscriptionCollection coll = (CustomerSubscriptionCollection) result;
        Iterator<Subscription> it = coll.getData().iterator();
        boolean foundSubscription = false;
        while (it.hasNext()) {
        	Subscription sub = it.next();
            if (sub.getId().equals(this.subscriptionId)){
            	foundSubscription = true;
            }
        }
        assertTrue(foundSubscription);
    }    

	@Category({
	    RegressionTests.class,
	    SmokeTests.class
	})
	@Test
	public void testListActiveSubscriptionsWithLimit()
	    throws Exception
	{
		upsertOnTestRunMessage("limit", "1");
		Object result = runFlowAndGetPayload("list-active-subscriptions");
	    assertNotNull(result);
	    CustomerSubscriptionCollection coll = (CustomerSubscriptionCollection)result;	    
	    assertEquals(1, coll.getData().size());        
	}

}
