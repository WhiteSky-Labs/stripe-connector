
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import com.stripe.model.Coupon;
import com.stripe.model.Plan;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class UpdateCouponTestCases
    extends StripeTestParent
{


	private String couponId;

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createCouponTestData");
    	Object result = runFlowAndGetPayload("create-coupon");
    	Coupon coupon = (Coupon) result;
    	this.couponId = coupon.getId();
        initializeTestRunMessage("updateCouponTestData");
        upsertOnTestRunMessage("id", this.couponId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	initializeTestRunMessage("deleteCouponTestData");
        upsertOnTestRunMessage("id", this.couponId);
        runFlowAndGetPayload("delete-coupon");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testUpdateCoupon()
        throws Exception
    {
    	Map<String, Object> expectedBean = getBeanFromContext("updateCouponTestData");
    	upsertOnTestRunMessage("id", this.couponId);
        Object result = runFlowAndGetPayload("update-coupon");
        assertNotNull(result);
        Coupon coupon = (Coupon) result;
        assertEquals(expectedBean.get("metadata"), coupon.getMetadata());
    }

}
