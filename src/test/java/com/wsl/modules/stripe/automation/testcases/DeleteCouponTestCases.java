
package com.wsl.modules.stripe.automation.testcases;

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

public class DeleteCouponTestCases
    extends StripeTestParent
{


	private String couponId;

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createCouponTestData");    	       
        Object result = runFlowAndGetPayload("create-coupon");
        Coupon coupon = (Coupon)result;
        this.couponId = coupon.getId();
        initializeTestRunMessage("deleteCouponTestData");
        upsertOnTestRunMessage("id", this.couponId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	try {
    		initializeTestRunMessage("retrieveCouponTestData");
    		upsertOnTestRunMessage("id", this.couponId);
    		Object result = runFlowAndGetPayload("retrieve-coupon");
            Coupon coupon = (Coupon) result;
            if (coupon != null) {
            	initializeTestRunMessage("deleteCouponTestData");
            	upsertOnTestRunMessage("id", this.couponId);
            	runFlowAndGetPayload("delete-coupon");
            }
    	} catch (MessagingException e){
    		// this is expected, if the coupon doesn't exist.
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeleteCoupon()
        throws Exception
    {
        Object result = runFlowAndGetPayload("delete-coupon");
        /*
         * Result will be of the format 
         * { "deleted": true, "id": "pln_5yv1Swh5U1jp4P" }
         */
        assertTrue(result.toString().contains("\"deleted\": true"));
        assertTrue(result.toString().contains(this.couponId));
        // Confirm the coupon doesn't exist
        initializeTestRunMessage("retrieveCouponTestData");
        upsertOnTestRunMessage("id", this.couponId);
        
        try {
    		runFlowAndGetPayload("retrieve-coupon");
    		fail("Retrieving a coupon that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the coupon"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeleteCouponThatDoesntExist()
        throws Exception
    {
        upsertOnTestRunMessage("id", "InvalidID");
    	try {
    		runFlowAndGetPayload("delete-coupon");
    		fail("Deleting a coupon that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not delete the coupon"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
