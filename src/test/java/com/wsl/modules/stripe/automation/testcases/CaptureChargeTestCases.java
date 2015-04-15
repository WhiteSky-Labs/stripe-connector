/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import com.stripe.model.Charge;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CaptureChargeTestCases
    extends StripeTestParent
{

	String chargeId;

    @Before
    public void setup()
        throws Exception
    {
        initializeTestRunMessage("createChargeTestData");
        upsertOnTestRunMessage("capture", false);
        Object result = runFlowAndGetPayload("create-charge");
        Charge charge = (Charge) result;
        this.chargeId = charge.getId();
        initializeTestRunMessage("captureChargeTestData");
        upsertOnTestRunMessage("id", this.chargeId);
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCaptureCharge()
        throws Exception
    {
        Object result = runFlowAndGetPayload("capture-charge");
        Charge charge = (Charge)result;
        Map<String, String> expectedBean = getBeanFromContext("createChargeTestData");
        assertEquals(expectedBean.get("amount"), charge.getAmount().toString());
        assertEquals(expectedBean.get("currency"), charge.getCurrency());
        assertEquals(true, charge.getCaptured().booleanValue());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCaptureChargeThatDoesntExist()
        throws Exception
    {
    	upsertOnTestRunMessage("id", "InvalidID");
        try{
        	runFlowAndGetPayload("capture-charge");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not capture the Charge"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
}
