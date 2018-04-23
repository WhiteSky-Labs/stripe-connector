/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.*;

import com.stripe.model.Customer;
import com.stripe.model.StripeObject;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class DeleteCustomerTestCases
    extends StripeTestParent
{

	private String customerId;

    @Before
    public void setup()
        throws Exception
    {
        Object result = runFlowAndGetPayload("create-customer", "createCustomerTestData");
        Customer customer = (Customer)result;
        this.customerId = customer.getId();
        initializeTestRunMessage("deleteCustomerTestData");
        upsertOnTestRunMessage("id", customerId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	initializeTestRunMessage("retrieveCustomerTestData");
        upsertOnTestRunMessage("id", this.customerId);
        
        Object result = runFlowAndGetPayload("retrieve-customer");
        Customer cust = (Customer) result;
        if (cust != null && (cust.getDeleted() == null || !cust.getDeleted())) {
        	initializeTestRunMessage("deleteCustomerTestData");
        	upsertOnTestRunMessage("id", this.customerId);
        	runFlowAndGetPayload("delete-customer");
        }
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeleteCustomer()
        throws Exception
    {
        Object result = runFlowAndGetPayload("delete-customer");
        /*
         * Result will be of the format 
         * { "deleted": true, "id": "cus_5yv1Swh5U1jp4P" }
         */
        assertTrue(result.toString().contains("\"deleted\": true"));
        assertTrue(result.toString().contains(this.customerId));
        // Confirm the customer doesn't exist
        initializeTestRunMessage("retrieveCustomerTestData");
        upsertOnTestRunMessage("id", this.customerId);
        
        result = runFlowAndGetPayload("retrieve-customer");
        Customer cust = (Customer) result;
        assertTrue(cust.getDeleted());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeleteCustomerThatDoesntExist()
        throws Exception
    {
        upsertOnTestRunMessage("id", "InvalidID");
    	try {
    		runFlowAndGetPayload("delete-customer");
    		fail("Deleting an customer that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not delete the customer"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
