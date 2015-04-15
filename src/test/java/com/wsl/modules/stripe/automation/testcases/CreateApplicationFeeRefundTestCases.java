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

public class CreateApplicationFeeRefundTestCases
    extends StripeTestParent
{
	private String chargeId;
	private String applicationFee = "100";
	private String feeId;
	
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
        result = runFlowAndGetPayload("list-all-application-fees");
        ApplicationFeeCollection coll = (ApplicationFeeCollection)result;
        ApplicationFee fee = coll.getData().get(0);
        this.feeId = fee.getId();
        initializeTestRunMessage("createApplicationFeeRefundTestData");
        upsertOnTestRunMessage("id", this.feeId);        
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateApplicationFeeRefund()
        throws Exception
    {
        Object result = runFlowAndGetPayload("create-application-fee-refund");
        assertNotNull(result);
        ApplicationFee fee = (ApplicationFee)result;
        assertTrue(fee.getRefunds().getData().size() == 1);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateInvalidApplicationFeeRefund()
        throws Exception
    {
    	upsertOnTestRunMessage("id", "InvalidID");
    	try{
    		runFlowAndGetPayload("create-application-fee-refund");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not refund Application Fee"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    
    }
    
}
