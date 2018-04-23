/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
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

public class RetrieveBitcoinReceiverTestCases
    extends StripeTestParent
{
	private String bcrId;
	
    @Before
    public void setup()
        throws Exception
    {        
        initializeTestRunMessage("createBitcoinReceiverTestData");
        String email = "test" + UUID.randomUUID() + "fill_never" + "@gmail.com";
    	upsertOnTestRunMessage("email", email);
    	Object result = runFlowAndGetPayload("create-bitcoin-receiver");        
        BitcoinReceiver bcr = (BitcoinReceiver) result;
        this.bcrId = bcr.getId();
        initializeTestRunMessage("retrieveBitcoinReceiverTestData");
        upsertOnTestRunMessage("id", this.bcrId);
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveBitcoinReceiver()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-bitcoin-receiver");
        assertNotNull(result);
        BitcoinReceiver bcr = (BitcoinReceiver) result;
        assertEquals(this.bcrId, bcr.getId());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveInvalidBitcoinReceiver()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-bitcoin-receiver");
            fail("Getting a bitcoin receiver that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve Bitcoin Receiver"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }
}
