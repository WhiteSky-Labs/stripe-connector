
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
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

	private String transactionId = "txn_15oTnK2aSsQzRUZqLP87dhIH";
	
    @Before
    public void setup()
        throws Exception
    {
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
    public void testListAllBalanceTransactions()
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
    public void testListBalanceTransactionsWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-balance-history");
        assertNotNull(result);
        BalanceTransactionCollection coll = (BalanceTransactionCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }

}
