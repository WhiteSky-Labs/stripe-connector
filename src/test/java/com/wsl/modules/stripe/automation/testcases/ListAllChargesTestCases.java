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
import static org.junit.Assert.fail;

import java.util.Iterator;

import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Coupon;
import com.stripe.model.CouponCollection;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class ListAllChargesTestCases
    extends StripeTestParent
{

	private String chargeId;

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createChargeTestData");
    	Object result = runFlowAndGetPayload("create-charge");
    	Charge charge = (Charge) result;
    	this.chargeId = charge.getId();
        initializeTestRunMessage("listAllChargesTestData");
    }    

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListAllCharges()
        throws Exception
    {
        Object result = runFlowAndGetPayload("list-all-charges");
        assertNotNull(result);
        ChargeCollection coll = (ChargeCollection)result;
        Iterator<Charge> it = coll.getData().iterator();
        boolean foundCharge = false;
        while (it.hasNext()) {
            Charge charge = it.next();
            if (charge.getId().equals(this.chargeId)){
            	foundCharge = true;
            }
        }
        assertTrue(foundCharge);
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListChargesWithLimit()
        throws Exception
    {
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-charges");
        assertNotNull(result);
        ChargeCollection coll = (ChargeCollection)result;
        
        assertEquals(1, coll.getData().size());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListChargesForInvalidCustomer()
        throws Exception
    {
    	upsertOnTestRunMessage("customer", "InvalidID");
    	try{
    		runFlowAndGetPayload("list-all-charges");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not list the charges"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testListChargesWithCreated()
        throws Exception
    {
    	initializeTestRunMessage("listAllChargesWithCreatedTestData");
    	upsertOnTestRunMessage("limit", "1");
    	Object result = runFlowAndGetPayload("list-all-charges");
        assertNotNull(result);
        ChargeCollection coll = (ChargeCollection)result;
        
        assertEquals(1, coll.getData().size());
    }
}
