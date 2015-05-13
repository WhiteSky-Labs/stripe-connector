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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.UUID;

import com.stripe.model.Charge;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.CreateAccountParameters;
import com.wsl.modules.stripe.complextypes.CreateChargeParameters;

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
        
        Map<String, Object> chargeData = getBeanFromContext("createChargeTestData");
    	CreateChargeParameters chargeParams = (CreateChargeParameters) chargeData.get("createChargeParameters");
    	chargeParams.setCapture(false);
    	upsertOnTestRunMessage("createChargeParameters", chargeParams);	
    	
        
        
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
        Map<String, Object> expectedBean = getBeanFromContext("createChargeTestData");
        CreateChargeParameters params = (CreateChargeParameters) expectedBean.get("createChargeParameters");
        assertEquals(Integer.toString(params.getAmount()), charge.getAmount().toString());
        assertEquals(params.getCurrency(), charge.getCurrency());
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
