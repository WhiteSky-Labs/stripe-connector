/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateCustomerTestCases
    extends StripeTestParent
{
	private List<String> customerIds = new ArrayList<String>();
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createCustomerTestData");
    	
    }

    @After
    public void tearDown()
        throws Exception
    {
        initializeTestRunMessage("deleteCustomerTestData");
        for (String customerId : customerIds){
        	upsertOnTestRunMessage("id", customerId);
        	runFlowAndGetPayload("delete-customer");
        }
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCustomer()
        throws Exception
    {
    	Object result = runFlowAndGetPayload("create-customer");
        assertNotNull(result);
        Customer customer = (Customer) result;
        assertNotNull(customer.getId());
        assertTrue(customer.getId().length() > 0);
        this.customerIds.add(customer.getId());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCustomerWithValidOptionalParams()
        throws Exception
    {
    	Object result = runFlowAndGetPayload("create-customer", "createCustomerWithValidParamsTestData");
    	Map<String, Object> expectedBean = getBeanFromContext("createCustomerWithValidParamsTestData");
    	assertNotNull(result);
        Customer customer = (Customer) result;
        assertNotNull(customer.getId());
        assertTrue(customer.getId().length() > 0);
        this.customerIds.add(customer.getId());
        assertEquals(expectedBean.get("email"), customer.getEmail());
        assertEquals(expectedBean.get("metadata"), customer.getMetadata());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCustomerWithInvalidOptionalParams()
        throws Exception
    {
    	try{
    		Object result = runFlowAndGetPayload("create-customer", "createCustomerWithInvalidParamsTestData");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		// NumberFormatException for invalid integer
    		assertTrue(e.getCause().getMessage().contains("For input string"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
}
