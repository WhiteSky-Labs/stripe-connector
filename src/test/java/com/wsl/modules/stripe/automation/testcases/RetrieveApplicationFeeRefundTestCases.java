
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
import com.stripe.model.FeeRefund;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class RetrieveApplicationFeeRefundTestCases
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
        initializeTestRunMessage("retrieveApplicationFeeRefundTestData");
        upsertOnTestRunMessage("id", this.refundId);
        upsertOnTestRunMessage("fee", this.feeId);
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveApplicationFeeRefund()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-application-fee-refund");
        assertNotNull(result);
        FeeRefund refund = (FeeRefund)result;
        assertEquals(this.applicationFee, refund.getAmount().toString());
        assertEquals(this.refundId, refund.getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentApplicationFeeRefund()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-application-fee-refund");
            fail("Getting a fee refund that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve Application Fee Refund"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }
    
}
