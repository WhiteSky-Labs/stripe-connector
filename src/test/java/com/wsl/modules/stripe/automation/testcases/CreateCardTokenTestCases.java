/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertNotNull;

import com.stripe.model.Card;
import com.stripe.model.Token;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class CreateCardTokenTestCases
    extends StripeTestParent
{


    @Before
    public void setup()
        throws Exception
    {
        initializeTestRunMessage("createCardTokenTestData");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCardToken()
        throws Exception
    {
        Object result = runFlowAndGetPayload("create-card-token");
        assertNotNull(result);
        Token token = (Token) result;
        assertNotNull(token.getId());
    }

}