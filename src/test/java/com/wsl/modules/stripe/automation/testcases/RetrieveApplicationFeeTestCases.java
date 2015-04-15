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

import java.util.UUID;

import com.stripe.model.Account;
import com.stripe.model.ApplicationFee;
import com.stripe.model.ApplicationFeeCollection;
import com.stripe.model.Charge;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class RetrieveApplicationFeeTestCases
    extends StripeTestParent
{

	private String feeId;
	private String applicationFee = "100";

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createAccountTestData");
    	String email = "test" + UUID.randomUUID() + "@gmail.com";
    	upsertOnTestRunMessage("email", email);
    	Account account = (Account) runFlowAndGetPayload("create-account");
    	initializeTestRunMessage("createChargeTestData");
    	 
    	upsertOnTestRunMessage("applicationFee", applicationFee);
    	upsertOnTestRunMessage("destination", account.getId());
    	Object result = runFlowAndGetPayload("create-charge");
    	Charge charge = (Charge) result;
    	    	
        initializeTestRunMessage("listAllApplicationFeesTestData");
        /*
         * Note: at this point, we would normally filter the list by the charge ID
         * However, while the endpoint to create a charge with a fee is working, this API (list all) doesn't match the charge ID
         * This can be verified in the console. 
         * The fees object appears to use a different ID format for charges.
         * Until this bug is resolved, The test will just check that a fee is returned.
         * 
         */
        result = runFlowAndGetPayload("list-all-application-fees");
        ApplicationFeeCollection coll = (ApplicationFeeCollection)result;
        ApplicationFee fee = coll.getData().get(0);
        this.feeId = fee.getId();
        initializeTestRunMessage("retrieveApplicationFeeTestData");
        upsertOnTestRunMessage("id", fee.getId());
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveApplicationFee()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-application-fee");
        assertNotNull(result);
        ApplicationFee fee = (ApplicationFee) result;
        assertEquals(this.feeId, fee.getId());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentFee()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-application-fee");
            fail("Getting a fee that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Application Fee"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }
    
}
