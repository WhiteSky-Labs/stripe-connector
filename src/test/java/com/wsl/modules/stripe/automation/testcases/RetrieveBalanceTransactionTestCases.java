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

import com.stripe.model.BalanceTransaction;
import com.stripe.model.Coupon;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class RetrieveBalanceTransactionTestCases
    extends StripeTestParent
{
    @Before
    public void setup()
        throws Exception
    {    	
        initializeTestRunMessage("retrieveBalanceTransactionTestData");      
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveBalanceTransaction()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-balance-transaction");
        assertNotNull(result);
        BalanceTransaction transaction = (BalanceTransaction) result;
        assertEquals("usd", transaction.getCurrency());
        assertEquals(12300, transaction.getAmount().intValue());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentTransaction()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-balance-transaction");
            fail("Getting a balance transaction that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Balance Transaction"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
