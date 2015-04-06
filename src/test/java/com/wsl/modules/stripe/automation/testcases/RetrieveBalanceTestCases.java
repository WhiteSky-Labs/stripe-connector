
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import com.stripe.model.Balance;
import com.stripe.model.Coupon;
import com.stripe.model.Money;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class RetrieveBalanceTestCases
    extends StripeTestParent
{


	@Before
    public void setup()
        throws Exception
    {
		initializeTestRunMessage("retrieveBalanceTestData");
    }

    @After
    public void tearDown()
        throws Exception
    {
    	
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveBalance()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-balance");
        assertNotNull(result);
        Balance balance = (Balance) result;        
        assertEquals(false, balance.getLivemode().booleanValue());
        List<Money> available = balance.getAvailable();
        Money availableMoney = available.get(0);
        assertEquals("usd", availableMoney.getCurrency());
    }

}
