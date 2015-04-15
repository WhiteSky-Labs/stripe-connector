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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateCardTestCases
    extends StripeTestParent
{

	private String customerId;
	private String cardId;
	
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
        
    }

    @After
    public void tearDown()
        throws Exception
    {
    	if (cardId != null){
	    	initializeTestRunMessage("deleteCardTestData");
	    	upsertOnTestRunMessage("ownerId", customerId);
	    	upsertOnTestRunMessage("id", cardId);
	    	runFlowAndGetPayload("delete-card");
    	}
        initializeTestRunMessage("deleteCustomerTestData");
        upsertOnTestRunMessage("id", customerId);
        runFlowAndGetPayload("delete-customer");   
        this.cardId = null;
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCard()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("create-card");
        assertNotNull(result);
        Card card = (Card) result;
        assertNotNull(card.getId());
        this.cardId = card.getId();
        Map<String, Object> expectedBean = getBeanFromContext("createCardTestData");
        Source source = (Source) expectedBean.get("source");
    	assertEquals(source.getExpMonth(), card.getExpMonth().toString());
    	assertEquals("20" + source.getExpYear(), card.getExpYear().toString());
    }
        
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCardWithInvalidDetails()
        throws Exception
    {
    	try{
    		runFlowAndGetPayload("create-card", "createCardInvalidTestData");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		// NumberFormatException for invalid integer
    		assertTrue(e.getCause().getMessage().contains("Could not create the Card"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
}
