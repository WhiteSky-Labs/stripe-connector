
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class RetrieveRefundTestCases
    extends StripeTestParent
{
	private String chargeId;
	private String refundId;

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createChargeTestData");
    	Object result = runFlowAndGetPayload("create-charge");
    	Charge charge = (Charge) result;
    	this.chargeId = charge.getId();
        initializeTestRunMessage("createRefundTestData");
        upsertOnTestRunMessage("id", this.chargeId);
        result = runFlowAndGetPayload("create-refund");
        Refund refund = (Refund) result;
        this.refundId = refund.getId();
        initializeTestRunMessage("retrieveRefundTestData");
        upsertOnTestRunMessage("chargeId", this.chargeId);
        upsertOnTestRunMessage("id", this.refundId);
    }    
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveRefund()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-refund");
        Refund refund = (Refund) result;
        assertEquals(this.refundId, refund.getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentRefund()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-refund");
            fail("Getting a refund that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Refund"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
