
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.stripe.model.Card;
import com.stripe.model.Customer;
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

public class RetrieveSubscriptionTestCases
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
		initializeTestRunMessage("retrieveSubscriptionTestData");
		upsertOnTestRunMessage("customerId", this.customerId);
		upsertOnTestRunMessage("subscriptionId", this.subscriptionId);
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
    public void testRetrieveSubscription()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-subscription");
        assertNotNull(result);
        Subscription sub = (Subscription)result;
        assertEquals(this.subscriptionId, sub.getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentSubscription()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("subscriptionId", "InvalidID");
        	runFlowAndGetPayload("retrieve-subscription");
            fail("Getting a subscription that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Subscription"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}