/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.CreateSubscriptionParameters;
import com.wsl.modules.stripe.complextypes.ListAllBalanceHistoryParameters;
import com.wsl.modules.stripe.complextypes.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateSubscriptionTestCases
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
        Map<String, Object> tempData = getBeanFromContext("createSubscriptionTestData");
    	CreateSubscriptionParameters params = (CreateSubscriptionParameters) tempData.get("createSubscriptionParameters");
    	params.setCustomerId(customerId);
    	params.setPlan(this.planId);
    	
    	upsertOnTestRunMessage("createSubscriptionParameters", params);
    }

    @After
    public void tearDown()
        throws Exception
    {
		if (this.subscriptionId != null) {
			initializeTestRunMessage("cancelSubscriptionTestData");
			upsertOnTestRunMessage("customerId", customerId);
	    	upsertOnTestRunMessage("subscriptionId", subscriptionId);
	    	runFlowAndGetPayload("cancel-subscription");	    	
		}
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
    public void testCreateSubscription()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("create-subscription");
        assertNotNull(result);
        Subscription sub = (Subscription) result;
        assertNotNull(sub.getId());
        this.subscriptionId = sub.getId();
        Map<String, Object> expectedBean = getBeanFromContext("createSubscriptionTestData");
        CreateSubscriptionParameters params = (CreateSubscriptionParameters) expectedBean.get("createSubscriptionParameters");
        assertEquals(params.getMetadata(), sub.getMetadata());
        assertEquals(this.planId, ((Plan) sub.getPlan()).getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateInvalidSubscription()
        throws Exception
    {
    	initializeTestRunMessage("createSubscriptionInvalidTestData");
    	try{
    		Object result = runFlowAndGetPayload("create-subscription");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not create the Subscription"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateSubscriptionWithoutSource()
        throws Exception
    {
    	try{
    		initializeTestRunMessage("createSubscriptionWithoutSourceTestData");
    		Object result = runFlowAndGetPayload("create-subscription");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not create the Subscription"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
}
