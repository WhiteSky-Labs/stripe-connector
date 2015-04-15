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

import java.util.UUID;

import com.stripe.model.Account;
import com.stripe.model.ApplicationFee;
import com.stripe.model.ApplicationFeeCollection;
import com.stripe.model.Charge;
import com.stripe.model.FeeRefund;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class UpdateApplicationFeeRefundTestCases
    extends StripeTestParent
{


	private String applicationFee = "100";
	private String feeId;
	private String refundId;
	
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
        result = runFlowAndGetPayload("create-application-fee-refund");
        fee = (ApplicationFee)result;
        this.refundId = fee.getRefunds().getData().get(0).getId();
        initializeTestRunMessage("updateApplicationFeeRefundTestData");
        upsertOnTestRunMessage("id", this.refundId);
        upsertOnTestRunMessage("fee", this.feeId);
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testUpdateApplicationFeeRefund()
        throws Exception
    {
        Object result = runFlowAndGetPayload("update-application-fee-refund");
        assertNotNull(result);
        FeeRefund refund = (FeeRefund)result;
        assertEquals(this.applicationFee, refund.getAmount().toString());
        assertEquals(this.refundId, refund.getId());        
    }

}
