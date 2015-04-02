
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import com.stripe.model.Customer;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class UpdateCustomerTestCases
    extends StripeTestParent
{

	private String customerId;

    @Before
    public void setup()
        throws Exception
    {
    	runFlowAndGetPayload("create-customer", "createCustomerTestData");
        Object result = runFlowAndGetPayload("create-customer", "createCustomerTestData");
        Customer customer = (Customer)result;
        this.customerId = customer.getId();
        initializeTestRunMessage("updateCustomerTestData");
        upsertOnTestRunMessage("id", this.customerId);
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
    public void testUpdateCustomer()
        throws Exception
    {
    	Map<String, Object> expectedBean = getBeanFromContext("updateCustomerTestData");
    	upsertOnTestRunMessage("id", this.customerId);
        Object result = runFlowAndGetPayload("update-customer");
        assertNotNull(result);
        Customer cust = (Customer)result;
        assertEquals(expectedBean.get("email"), cust.getEmail());
    }

}
