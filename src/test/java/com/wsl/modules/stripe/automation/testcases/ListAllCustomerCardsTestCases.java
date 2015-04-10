/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import com.stripe.model.Card;
import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;
import com.stripe.model.PaymentSourceCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ListAllCustomerCardsTestCases
    extends StripeTestParent
{

	private String cardId;
	private String customerId;

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createCustomerTestData");
    	Object result = runFlowAndGetPayload("create-customer");
        Customer customer = (Customer) result;
        this.customerId = customer.getId();
        initializeTestRunMessage("createCardTestData");
        upsertOnTestRunMessage("ownerId", customerId);
        result = runFlowAndGetPayload("create-card");
        Card card = (Card) result;
        this.cardId = card.getId();
        initializeTestRunMessage("listAllCustomerCardsTestData");
        upsertOnTestRunMessage("id", this.cardId);
        upsertOnTestRunMessage("ownerId", this.customerId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	initializeTestRunMessage("deleteCardTestData");
    	upsertOnTestRunMessage("id", this.cardId);
    	upsertOnTestRunMessage("ownerId", this.customerId);
    	runFlowAndGetPayload("delete-card");
	    initializeTestRunMessage("deleteCustomerTestData");
	    upsertOnTestRunMessage("id", this.customerId);
	    runFlowAndGetPayload("delete-customer");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListAllCoupons()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-customer-cards");
        assertNotNull(result);
        PaymentSourceCollection coll = (PaymentSourceCollection)result;
        Iterator<PaymentSource> it = coll.getData().iterator();
        boolean foundCard = false;
        while (it.hasNext()) {
            PaymentSource source = it.next();
            if (source.getId().equals(this.cardId)){
            	foundCard = true;
            }
        }
        assertTrue(foundCard);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListCardsWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-customer-cards");
        assertNotNull(result);
        PaymentSourceCollection coll = (PaymentSourceCollection)result;
        assertEquals(1, coll.getData().size());        
    }
}
