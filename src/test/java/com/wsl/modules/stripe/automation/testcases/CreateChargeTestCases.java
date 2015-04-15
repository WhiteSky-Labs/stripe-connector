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

import static org.junit.Assert.*;

import java.util.Map;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateChargeTestCases
    extends StripeTestParent
{

	@Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createChargeTestData");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCharge()
        throws Exception
    {
        Object result = runFlowAndGetPayload("create-charge");
        Charge charge = (Charge) result;
        Map<String, String> expectedBean = getBeanFromContext("createChargeTestData");
        assertEquals(expectedBean.get("amount"), charge.getAmount().toString());
        assertEquals(expectedBean.get("currency"), charge.getCurrency());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateInvalidCharge()
        throws Exception
    {
    	upsertOnTestRunMessage("destination", "InvalidDestination");
        try{
        	runFlowAndGetPayload("create-charge");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not create the Charge"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }

    
}
