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

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.stripe.model.Account;
import com.stripe.model.ApplicationFee;
import com.stripe.model.ApplicationFeeCollection;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
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

public class ListAllApplicationFeesTestCases
    extends StripeTestParent
{

	private String chargeId;
	private String applicationFee = "100";

    @Before
    public void setup()
        throws Exception
    {
    	
    	initializeTestRunMessage("createAccountTestData");
    	String email = "test" + UUID.randomUUID() + "@gmail.com";
    	
    	Map<String, Object> accountData = getBeanFromContext("createAccountTestData");
    	CreateAccountParameters accountParams = (CreateAccountParameters) accountData.get("createAccountParameters");
    	accountParams.setEmail(email);
    	upsertOnTestRunMessage("createAccountParameters", accountParams);	
    	
    	
    	
    	Account account = (Account) runFlowAndGetPayload("create-account");
    	initializeTestRunMessage("createChargeTestData");
    	 
    	upsertOnTestRunMessage("applicationFee", applicationFee);
    	upsertOnTestRunMessage("destination", account.getId());
    	Object result = runFlowAndGetPayload("create-charge");
    	Charge charge = (Charge) result;
    	this.chargeId = charge.getId();    	
        initializeTestRunMessage("listAllApplicationFeesTestData");
        /*
         * Note: at this point, we would normally filter the list by the charge ID
         * However, while the endpoint to create a charge with a fee is working, this API (list all) doesn't match the charge ID
         * This can be verified in the console. 
         * The fees object appears to use a different ID format for charges.
         * Until this bug is resolved, The test will just check that a fee is returned.
         * 
         */
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListAllApplicationFees()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-application-fees");
        assertNotNull(result);
        ApplicationFeeCollection coll = (ApplicationFeeCollection)result;
        assertTrue(coll.getData().size() > 0);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListApplicationFeesWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-application-fees");
        assertNotNull(result);
        ApplicationFeeCollection coll = (ApplicationFeeCollection)result;
        
        assertEquals(1, coll.getData().size());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListApplicationFeesForInvalidChargeID()
        throws Exception
    {
    	upsertOnTestRunMessage("charge", "InvalidID");
	
		Object result = runFlowAndGetPayload("list-all-application-fees");
        assertNotNull(result);
        ApplicationFeeCollection coll = (ApplicationFeeCollection)result;
        
        assertEquals(0, coll.getData().size());
    	 
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListApplicationFeesWithCreated()
        throws Exception
    {
    	initializeTestRunMessage("listAllApplicationFeesWithCreatedTestData");
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-application-fees");
        assertNotNull(result);
        ApplicationFeeCollection coll = (ApplicationFeeCollection)result;
        
        assertEquals(1, coll.getData().size());
    }
}
