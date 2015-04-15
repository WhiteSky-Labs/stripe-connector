/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
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
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateCouponTestCases
    extends StripeTestParent
{

	private List<String> couponIds = new ArrayList<String>();
	
    @Before
    public void setup()
        throws Exception
    {    	
    	initializeTestRunMessage("createCouponTestData");
    	
    }

    @After
    public void tearDown()
        throws Exception
    {
        initializeTestRunMessage("deleteCouponTestData");
        for (String couponId : couponIds){
        	upsertOnTestRunMessage("id", couponId);
        	runFlowAndGetPayload("delete-coupon");
        }
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCoupon()
        throws Exception
    {
    	Object result = runFlowAndGetPayload("create-coupon");
        assertNotNull(result);
        Coupon coupon = (Coupon) result;
        assertNotNull(coupon.getId());
        assertTrue(coupon.getId().length() > 0);
        this.couponIds.add(coupon.getId());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCouponWithValidOptionalParams()
        throws Exception
    {
    	Object result = runFlowAndGetPayload("create-coupon", "createCouponWithValidParamsTestData");
    	Map<String, Object> expectedBean = getBeanFromContext("createCouponWithValidParamsTestData");
    	assertNotNull(result);
        Coupon coupon = (Coupon) result;
        assertNotNull(coupon.getId());
        assertTrue(coupon.getId().length() > 0);
        this.couponIds.add(coupon.getId());
        assertEquals(expectedBean.get("durationInMonths"), coupon.getDurationInMonths().toString());
        assertEquals(expectedBean.get("maxRedemptions"), coupon.getMaxRedemptions().toString());
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateCouponWithInvalidParams()
        throws Exception
    {
    	initializeTestRunMessage("createCouponWithInvalidParamsTestData");
        try{
        	runFlowAndGetPayload("create-coupon");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not create the coupon"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
}
