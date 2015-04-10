/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertNotNull;

import com.stripe.model.Token;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class CreateBankAccountTokenTestCases
    extends StripeTestParent
{


    @Before
    public void setup()
        throws Exception
    {        
        initializeTestRunMessage("createBankAccountTokenTestData");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateBankAccountToken()
        throws Exception
    {
        Object result = runFlowAndGetPayload("create-bank-account-token");
        assertNotNull(result);
        Token token = (Token) result;
        assertNotNull(token.getId());        
    }

}
