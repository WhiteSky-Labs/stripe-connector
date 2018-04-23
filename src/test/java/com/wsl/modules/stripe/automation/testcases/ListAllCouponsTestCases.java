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

import java.util.Iterator;

import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ListAllCouponsTestCases
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
        initializeTestRunMessage("listAllCouponsTestData");
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
    public void testListAllCoupons()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-coupons");
        assertNotNull(result);
        CouponCollection coll = (CouponCollection)result;
        Iterator<Coupon> it = coll.getData().iterator();
        boolean foundCoupon = false;
        while (it.hasNext()) {
            Coupon coupon = it.next();
            if (coupon.getId().equals(this.couponId)){
            	foundCoupon = true;
            }
        }
        assertTrue(foundCoupon);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListCouponsWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-coupons");
        assertNotNull(result);
        CouponCollection coll = (CouponCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListCouponsWithCreated()
        throws Exception
    {
    	initializeTestRunMessage("listAllCouponsCreatedTestData");
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-coupons");
        assertNotNull(result);
        CouponCollection coll = (CouponCollection)result;
        
        assertEquals(1, coll.getData().size());        
    }
}
