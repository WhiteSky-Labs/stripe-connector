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

import java.util.Map;
import java.util.UUID;

import com.stripe.model.Account;
import com.stripe.model.ApplicationFee;
import com.stripe.model.ApplicationFeeCollection;
import com.stripe.model.Charge;
import com.stripe.model.FeeRefundCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;
import com.wsl.modules.stripe.complextypes.CreateAccountParameters;
import com.wsl.modules.stripe.complextypes.CreateChargeParameters;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class ListAllApplicationFeeRefundsTestCases
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
    	Map<String, Object> accountData = getBeanFromContext("createAccountTestData");
    	CreateAccountParameters accountParams = (CreateAccountParameters) accountData.get("createAccountParameters");
    	accountParams.setEmail("test"+ UUID.randomUUID() + "@gmail.com");
    	upsertOnTestRunMessage("createAccountParameters", accountParams);	
    	
    	Account account = (Account) runFlowAndGetPayload("create-account");
    	initializeTestRunMessage("createChargeTestData");
    	
    	
    	Map<String, Object> chargeData = getBeanFromContext("createChargeTestData");
    	CreateChargeParameters chargeParams = (CreateChargeParameters) chargeData.get("createChargeParameters");
    	chargeParams.setApplicationFee(Integer.parseInt(applicationFee));
    	chargeParams.setDestination(account.getId());
    	upsertOnTestRunMessage("createChargeParameters", chargeParams);	
    	
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
        initializeTestRunMessage("listAllApplicationFeeRefundsTestData");
        upsertOnTestRunMessage("id", this.feeId);                
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListAllApplicationFeeRefunds()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-application-fee-refunds");
        assertNotNull(result);
        FeeRefundCollection coll = (FeeRefundCollection)result;
        assertTrue(coll.getData().size() > 0);
        assertEquals(this.refundId, coll.getData().get(0).getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListApplicationFeeRefundsWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-application-fee-refunds");
        assertNotNull(result);
        FeeRefundCollection coll = (FeeRefundCollection)result;
        
        assertEquals(1, coll.getData().size());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListInvalidRefunds()
        throws Exception
    {
    	upsertOnTestRunMessage("id", "InvalidID");
    	try{
    		runFlowAndGetPayload("list-all-application-fee-refunds");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not list Application Fee Refunds"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    
    }
}
