/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ListAllCustomersTestCases
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
        
    	initializeTestRunMessage("listAllCustomersTestData");
    }

    @After
    public void tearDown()
        throws Exception
    {
        initializeTestRunMessage("deleteCustomerTestData");
        upsertOnTestRunMessage("id", this.customerId);
        runFlowAndGetPayload("delete-customer");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListAllCustomers()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-customers");
        assertNotNull(result);
        CustomerCollection coll = (CustomerCollection)result;
        Iterator<Customer> it = coll.getData().iterator();
        boolean foundCustomer = false;
        while (it.hasNext()) {
            Customer cust = it.next();
            if (cust.getId().equals(this.customerId)){
            	foundCustomer = true;
            }
        }
        assertTrue(foundCustomer);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListCustomersWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-customers");
        assertNotNull(result);
        CustomerCollection coll = (CustomerCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }

}
