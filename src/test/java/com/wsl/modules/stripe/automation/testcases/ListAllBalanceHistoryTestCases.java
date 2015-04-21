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

import java.util.Iterator;

import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ListAllBalanceHistoryTestCases
    extends StripeTestParent
{

	private String transactionId;
	
    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createChargeTestData");
    	Charge charge = (Charge) runFlowAndGetPayload("create-charge");
    	this.transactionId = charge.getBalanceTransaction();
    	initializeTestRunMessage("listAllBalanceHistoryTestData");
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
    public void testListAllBalanceHistory()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-balance-history");
        assertNotNull(result);
        BalanceTransactionCollection coll = (BalanceTransactionCollection) result;
        Iterator<BalanceTransaction> it = coll.getData().iterator();
        boolean foundTransaction = false;
        while (it.hasNext()) {
            BalanceTransaction transaction = it.next();
            if (transaction.getId().equals(this.transactionId)){
            	foundTransaction = true;
            }
        }
        assertTrue(foundTransaction);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListBalanceHistoryWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-balance-history");
        assertNotNull(result);
        BalanceTransactionCollection coll = (BalanceTransactionCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListBalanceHistoryWithAvailableOn()
        throws Exception
    {
    	initializeTestRunMessage("listAllBalanceHistoryWithAvailableOnTestData");
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-balance-history");
        assertNotNull(result);
        BalanceTransactionCollection coll = (BalanceTransactionCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListBalanceHistoryWithCreated()
        throws Exception
    {
    	initializeTestRunMessage("listAllBalanceHistoryWithCreatedTestData");
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-balance-history");
        assertNotNull(result);
        BalanceTransactionCollection coll = (BalanceTransactionCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }
}
