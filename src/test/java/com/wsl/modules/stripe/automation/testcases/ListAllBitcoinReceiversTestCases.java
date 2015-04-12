/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.UUID;

import com.stripe.model.BitcoinReceiver;
import com.stripe.model.BitcoinReceiverCollection;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ListAllBitcoinReceiversTestCases
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
        initializeTestRunMessage("listAllBitcoinReceiversTestData");        
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveBitcoinReceiver()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-bitcoin-receivers");
        assertNotNull(result);
        BitcoinReceiverCollection coll = (BitcoinReceiverCollection) result;
        Iterator<BitcoinReceiver> it = coll.getData().iterator();
        boolean found = false;
        while (it.hasNext()) {
            BitcoinReceiver bcr = it.next();
            if (bcr.getId().equals(this.bcrId)){
            	found = true;
            }
        }
        assertTrue(found);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListBitcoinReceiversWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-bitcoin-receivers");
        assertNotNull(result);
        BitcoinReceiverCollection coll = (BitcoinReceiverCollection)result;
        
        assertEquals(1, coll.getData().size());
    }

}
