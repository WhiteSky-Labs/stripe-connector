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
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.CreateSubscriptionParameters;
import com.wsl.modules.stripe.complextypes.UpdateSubscriptionParameters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class UpdateSubscriptionTestCases
    extends StripeTestParent
{


	private String customerId;
	private String subscriptionId;	
	private String planId;
	private String updatedPlanId;
	
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
    	result = runFlowAndGetPayload("create-subscription");
        Subscription sub = (Subscription) result;
		this.subscriptionId = sub.getId();
		initializeTestRunMessage("createPlanTestData");
		upsertOnTestRunMessage("id", "UpdatedTestPlan");
        result = runFlowAndGetPayload("create-plan");
        plan = (Plan)result;
        this.updatedPlanId = plan.getId();
        
		initializeTestRunMessage("updateSubscriptionTestData");
		Map<String, Object> tempSubscriptionData = getBeanFromContext("updateSubscriptionTestData");
    	UpdateSubscriptionParameters updateParams = (UpdateSubscriptionParameters) tempSubscriptionData.get("updateSubscriptionParameters");
    	updateParams.setCustomerId(this.customerId);
    	updateParams.setSubscriptionId(this.subscriptionId);
    	updateParams.setPlan(this.updatedPlanId);
    	upsertOnTestRunMessage("updateSubscriptionParameters", updateParams);		
		
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
        initializeTestRunMessage("deletePlanTestData");
        upsertOnTestRunMessage("id", this.updatedPlanId);
        runFlowAndGetPayload("delete-plan");                
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testUpdateSubscription()
        throws Exception
    {
        Object result = runFlowAndGetPayload("update-subscription");
        Map<String, String> expectedBean = getBeanFromContext("updateSubscriptionTestData");
        Subscription sub = (Subscription) result; 
        assertEquals(this.updatedPlanId, sub.getPlan().getId());     
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testUpdateNonexistentSubscription()
        throws Exception
    {
    	try {
    		Map<String, Object> tempSubscriptionData = getBeanFromContext("updateSubscriptionTestData");
        	UpdateSubscriptionParameters updateParams = (UpdateSubscriptionParameters) tempSubscriptionData.get("updateSubscriptionParameters");
        	updateParams.setPlan("InvalidPlan");
        	updateParams.setSubscriptionId("InvalidId");
        	upsertOnTestRunMessage("updateSubscriptionParameters", updateParams);		
    		
        	Object result = runFlowAndGetPayload("update-subscription");
            fail("Updating a subscription that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not update the Subscription"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testUpdateSubscriptionWithoutSource()
        throws Exception
    {
		initializeTestRunMessage("updateSubscriptionWithoutSourceTestData");
		
		Map<String, Object> tempSubscriptionData = getBeanFromContext("updateSubscriptionWithoutSourceTestData");
    	UpdateSubscriptionParameters updateParams = (UpdateSubscriptionParameters) tempSubscriptionData.get("updateSubscriptionParameters");
    	updateParams.setCustomerId(this.customerId);
    	updateParams.setSubscriptionId(this.subscriptionId);
    	updateParams.setPlan(this.updatedPlanId);
    	upsertOnTestRunMessage("updateSubscriptionParameters", updateParams);		
		
		Object result = runFlowAndGetPayload("update-subscription");
        Subscription sub = (Subscription)result;
        assertNotNull(sub.getId());        
    }
}
