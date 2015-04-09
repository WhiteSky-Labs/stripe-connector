
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class PayInvoiceTestCases
    extends StripeTestParent
{

	/**
	 *  Unfortunately, since we can't simulate the creation of an invoice, we can't test paying it either. 
	 * 
	 */
	
	
    @Before
    public void setup()
        throws Exception
    {
        initializeTestRunMessage("payInvoiceTestData");
    }

    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testPayInvoice()
        throws Exception
    {
    	try{
    		runFlowAndGetPayload("pay-invoice");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not pay the Invoice"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    
    }

}
