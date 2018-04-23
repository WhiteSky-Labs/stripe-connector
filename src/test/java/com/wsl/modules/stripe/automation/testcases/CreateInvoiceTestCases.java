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
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.CreateSubscriptionParameters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateInvoiceTestCases
    extends StripeTestParent
{

	private String customerId;
	private String subscriptionId;	
	private String planId;
	private String invoiceId;
	private String cardId;
	
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
        Map<String, Object> subData = getBeanFromContext("createSubscriptionTestData");
        CreateSubscriptionParameters params = (CreateSubscriptionParameters) subData.get("createSubscriptionParameters");
        params.setCustomerId(customerId);
        params.setPlan(planId);
        upsertOnTestRunMessage("createSubscriptionParameters", params);
        result = runFlowAndGetPayload("create-subscription");
        Subscription sub = (Subscription)result;
        this.subscriptionId = sub.getId();
        initializeTestRunMessage("createInvoiceTestData");
        upsertOnTestRunMessage("customerId", this.customerId);
        upsertOnTestRunMessage("subscription", this.subscriptionId);
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
    public void testCreateInvoice()
        throws Exception
    {    	
    	// unfortunately, we can't test creating an invoice
    	// A customer has to incur a cost (i.e. a subscription period has to pass) before the invoice can be created
    	// This condition can't be simulated.
    	try{
    		runFlowAndGetPayload("create-invoice");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not create the Invoice"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    
        
    }

}
