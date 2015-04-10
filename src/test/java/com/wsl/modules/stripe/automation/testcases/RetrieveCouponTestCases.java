/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

public class RetrieveCouponTestCases
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
        initializeTestRunMessage("retrieveCouponTestData");
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
    public void testRetrieveCoupon()
        throws Exception
    {
        Object result = runFlowAndGetPayload("retrieve-coupon");
        assertNotNull(result);
        Coupon coupon = (Coupon)result;
        assertEquals(this.couponId, coupon.getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testRetrieveNonexistentCoupon()
        throws Exception
    {
    	try {
        	upsertOnTestRunMessage("id", "InvalidID");
            Object result = runFlowAndGetPayload("retrieve-coupon");
            fail("Getting a coupon that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the coupon"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
