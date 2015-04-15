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

import java.util.Map;
import java.util.UUID;

import com.stripe.model.Account;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class UpdateAccountTestCases
    extends StripeTestParent
{

	private String email;
	private String accountId;
	private Map<String, Object> expectedBean;
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createManagedAccountTestData");
    	email = "test" + UUID.randomUUID() + "@gmail.com";
    	upsertOnTestRunMessage("email", email);
    	Object result = runFlowAndGetPayload("create-account");
    	Account account = (Account) result;
    	this.accountId = account.getId();
    	expectedBean = getBeanFromContext("updateAccountTestData");
    	initializeTestRunMessage("updateAccountTestData");
    	upsertOnTestRunMessage("id", this.accountId);
    	email = "test" + UUID.randomUUID() + "@gmail.com";
    	upsertOnTestRunMessage("email", email);    	
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testUpdateAccount()
        throws Exception
    {    	
    	Object result = runFlowAndGetPayload("update-account");
        assertNotNull(result);
        Account account = (Account) result;
        assertNotNull(account.getId());
        assertEquals(this.accountId, account.getId());
        assertEquals(this.email, account.getEmail());        
    }
}