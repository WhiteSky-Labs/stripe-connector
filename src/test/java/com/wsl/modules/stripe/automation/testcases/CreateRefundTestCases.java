
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Refund;
import com.stripe.model.Subscription;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateRefundTestCases
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
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateRefund()
        throws Exception
    {
    	Object result = runFlowAndGetPayload("create-refund");
    	assertNotNull(result);
    	Refund refund = (Refund) result;
    	Map<String, String> expectedBean = getBeanFromContext("createRefundTestData");
        assertEquals(expectedBean.get("amount"), refund.getAmount().toString());
    }

}
