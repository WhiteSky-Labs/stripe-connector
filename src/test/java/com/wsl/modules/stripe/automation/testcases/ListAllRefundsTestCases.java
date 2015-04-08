
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.ChargeRefundCollection;
import com.stripe.model.Refund;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ListAllRefundsTestCases
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
        initializeTestRunMessage("listAllRefundsTestData");
        upsertOnTestRunMessage("chargeId", this.chargeId);
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListAllRefunds()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-refunds");
        assertNotNull(result);
        ChargeRefundCollection coll = (ChargeRefundCollection)result;
        Iterator<Refund> it = coll.getData().iterator();
        boolean foundRefund = false;
        while (it.hasNext()) {
            Refund refund= it.next();
            if (refund.getId().equals(this.refundId)){
            	foundRefund = true;
            }
        }
        assertTrue(foundRefund);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListRefundsWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-refunds");
        assertNotNull(result);
        ChargeRefundCollection coll = (ChargeRefundCollection)result;
        
        assertEquals(1, coll.getData().size());
    }

}
