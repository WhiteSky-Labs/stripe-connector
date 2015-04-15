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

import java.util.Map;
import java.util.UUID;

import com.stripe.model.Account;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.LegalEntity;
import com.wsl.modules.stripe.complextypes.Source;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateAccountTestCases
    extends StripeTestParent
{
	private String email;
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createAccountTestData");
    	email = "test" + UUID.randomUUID() + "@gmail.com";
    	upsertOnTestRunMessage("email", email);
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateAccount()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("create-account");
        assertNotNull(result);
        Account account = (Account) result;
        assertNotNull(account.getId());
        assertEquals(this.email, account.getEmail());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateAccountWithInvalidParams()
        throws Exception
    {
    	upsertOnTestRunMessage("country", "InvalidCountry");
        try{
        	runFlowAndGetPayload("create-account");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not create the Account"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateAccountWithValidParams()
        throws Exception
    {
    	initializeTestRunMessage("createAccountWithParamsTestData");
    	Map<String, Object> expectedBean = getBeanFromContext("createAccountWithParamsTestData");
    	upsertOnTestRunMessage("email", email);
    	Object result = runFlowAndGetPayload("create-account");
        assertNotNull(result);
        Account account = (Account) result;
        assertNotNull(account.getId());
        assertEquals(this.email, account.getEmail());
        // Note: The API does not return the Legal Entity at this time, even when it is created correctly. As such, the assertations for this test are minimalistic, but the full creation of Legal Entity has occurred.      
    }
}
