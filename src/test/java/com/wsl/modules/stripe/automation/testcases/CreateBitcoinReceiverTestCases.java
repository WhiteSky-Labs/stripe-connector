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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import com.stripe.model.BitcoinReceiver;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateBitcoinReceiverTestCases
    extends StripeTestParent
{

	private String email;
	
    @Before
    public void setup()
        throws Exception
    {        
        initializeTestRunMessage("createBitcoinReceiverTestData");
        String email = "test" + UUID.randomUUID() + "fill_never" + "@gmail.com";
    	upsertOnTestRunMessage("email", email);
    	this.email = email;
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateBitcoinReceiver()
        throws Exception
    {
        Object result = runFlowAndGetPayload("create-bitcoin-receiver");
        assertNotNull(result);
        BitcoinReceiver bcr = (BitcoinReceiver) result;
        assertEquals(this.email, bcr.getEmail());        
    }

}
