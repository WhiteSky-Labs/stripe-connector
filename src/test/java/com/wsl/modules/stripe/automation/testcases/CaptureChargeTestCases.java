
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import com.stripe.model.Charge;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class CaptureChargeTestCases
    extends StripeTestParent
{

	String chargeId;

    @Before
    public void setup()
        throws Exception
    {
        initializeTestRunMessage("createChargeTestData");
        upsertOnTestRunMessage("capture", false);
        Object result = runFlowAndGetPayload("create-charge");
        Charge charge = (Charge) result;
        this.chargeId = charge.getId();
        initializeTestRunMessage("captureChargeTestData");
        upsertOnTestRunMessage("id", this.chargeId);
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCaptureCharge()
        throws Exception
    {
        Object result = runFlowAndGetPayload("capture-charge");
        Charge charge = (Charge)result;
        Map<String, String> expectedBean = getBeanFromContext("createChargeTestData");
        assertEquals(expectedBean.get("amount"), charge.getAmount().toString());
        assertEquals(expectedBean.get("currency"), charge.getCurrency());
        assertEquals(true, charge.getCaptured().booleanValue());
    }
}
