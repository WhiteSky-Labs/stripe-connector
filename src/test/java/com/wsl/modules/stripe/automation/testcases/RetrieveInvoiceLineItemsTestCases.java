/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.InvoiceLineItemCollection;
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

public class RetrieveInvoiceLineItemsTestCases
    extends StripeTestParent
{


	private String invoiceId;
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
		initializeTestRunMessage("retrieveAllInvoicesTestData");
		upsertOnTestRunMessage("customerId", this.customerId);
    	result = runFlowAndGetPayload("retrieve-all-invoices");	    
	    InvoiceCollection coll = (InvoiceCollection) result;
	    this.invoiceId = coll.getData().get(0).getId();
	    initializeTestRunMessage("retrieveInvoiceLineItemsTestData");
	    upsertOnTestRunMessage("id", this.invoiceId);
	    
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
    public void testRetrieveInvoiceLineItems()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("retrieve-invoice-line-items");
        assertNotNull(result);
        InvoiceLineItemCollection coll = (InvoiceLineItemCollection)result;
        InvoiceLineItem lineItem = coll.getData().get(0);
        assertNotNull(lineItem.getId());
    }  
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentInvoiceLineItems()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-invoice-line-items");
            fail("Getting an Invoice that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Invoice Line Items"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
