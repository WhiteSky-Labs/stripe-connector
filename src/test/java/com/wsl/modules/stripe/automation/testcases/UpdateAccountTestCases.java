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

import java.util.Map;
import java.util.UUID;

import com.stripe.model.Account;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.ListAllBalanceHistoryParameters;
import com.wsl.modules.stripe.complextypes.UpdateAccountParameters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class UpdateAccountTestCases
    extends StripeTestParent
{

	private String accountId;
	private Map<String, Object> expectedBean;
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createManagedAccountTestData");
    	Object result = runFlowAndGetPayload("create-account");
    	Account account = (Account) result;
    	this.accountId = account.getId();
    	expectedBean = getBeanFromContext("updateAccountTestData");
    	initializeTestRunMessage("updateAccountTestData");
    	
    	UpdateAccountParameters params = (UpdateAccountParameters) expectedBean.get("updateAccountParameters");
    	params.setId(this.accountId);
    	upsertOnTestRunMessage("updateAccountParameters", params);    	
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
        UpdateAccountParameters params = (UpdateAccountParameters) expectedBean.get("updateAccountParameters");
        assertEquals(params.getEmail(), account.getEmail());        
    }
}