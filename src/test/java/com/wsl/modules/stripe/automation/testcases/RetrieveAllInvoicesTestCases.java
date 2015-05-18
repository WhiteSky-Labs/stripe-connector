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

import com.stripe.model.CouponCollection;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
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

public class RetrieveAllInvoicesTestCases
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
        result = runFlowAndGetPayload("create-subscription");
        Subscription sub = (Subscription) result;
		this.subscriptionId = sub.getId();
		initializeTestRunMessage("retrieveAllInvoicesTestData");
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
    public void testRetrieveAllInvoices()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("retrieve-all-invoices");
        assertNotNull(result);
        InvoiceCollection coll = (InvoiceCollection) result;
        assertNotNull(coll);
        assertTrue(coll.getData().size() > 0);
    }    

	@Category({
	    RegressionTests.class,
	    SmokeTests.class
	})
	@Test
	public void testRetrieveAllInvoicesWithLimit()
	    throws Exception
	{
		upsertOnTestRunMessage("limit", "1");
		Object result = runFlowAndGetPayload("retrieve-all-invoices");
	    assertNotNull(result);
	    InvoiceCollection coll = (InvoiceCollection)result;	    
	    assertEquals(1, coll.getData().size());        
	}

	@Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveAllInvoicesWithDate()
        throws Exception
    {
    	initializeTestRunMessage("retrieveAllInvoicesWithDateTestData");
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("retrieve-all-invoices");
        assertNotNull(result);
        InvoiceCollection coll = (InvoiceCollection)result;
        
        assertEquals(1, coll.getData().size());
    }
	
}