
package com.wsl.modules.stripe.automation.testcases;

import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class RetrieveCustomerTestCases
    extends StripeTestParent
{


    @Before
    public void setup()
        throws Exception
    {
        //TODO: Add setup required to run test or remove method
        initializeTestRunMessage("retrieveCustomerTestData");
    }

    @After
    public void tearDown()
        throws Exception
    {
        //TODO: Add code to reset sandbox state to the one before the test was run or remove
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveCustomer()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-customer");
        throw new RuntimeException("NOT IMPLEMENTED METHOD");
    }

}
