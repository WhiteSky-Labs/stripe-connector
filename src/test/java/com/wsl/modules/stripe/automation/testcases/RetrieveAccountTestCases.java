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
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.CreateAccountParameters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class RetrieveAccountTestCases
    extends StripeTestParent
{

	private String accountId;
	private String email;
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createAccountTestData");
    	email = "test" + UUID.randomUUID() + "@gmail.com";
    	
    	Map<String, Object> accountData = getBeanFromContext("createAccountTestData");
    	CreateAccountParameters accountParams = (CreateAccountParameters) accountData.get("createAccountParameters");
    	accountParams.setEmail(email);
    	upsertOnTestRunMessage("createAccountParameters", accountParams);	
    	
    	
    	Object result = runFlowAndGetPayload("create-account");
    	Account account = (Account) result;
    	this.accountId = account.getId();
    	initializeTestRunMessage("retrieveAccountTestData");
    	upsertOnTestRunMessage("id", this.accountId);
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveAccount()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("retrieve-account");
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
    public void testRetrieveNonexistentAccount()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-account");
            fail("Getting an account that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Account"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }
}
