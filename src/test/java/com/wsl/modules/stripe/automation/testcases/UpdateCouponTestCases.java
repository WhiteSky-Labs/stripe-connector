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
