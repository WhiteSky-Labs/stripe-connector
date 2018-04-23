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

import com.stripe.model.Card;
import com.stripe.model.Coupon;
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

public class RetrieveCardTestCases
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
        
        initializeTestRunMessage("retrieveCardTestData");
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
    public void testRetrieveCard()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-card");
        assertNotNull(result);
        Card card = (Card)result;
        assertEquals(this.cardId, card.getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentCard()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-card");
            fail("Getting a card that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Card"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
