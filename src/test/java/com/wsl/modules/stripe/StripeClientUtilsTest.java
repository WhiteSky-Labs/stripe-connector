package com.wsl.modules.stripe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.tests.ConnectorTestCase;

import com.wsl.modules.stripe.utils.StripeClientUtils;

public class StripeClientUtilsTest extends ConnectorTestCase {
	
	Map<String, Object> params = new HashMap<String, Object>();
	
    @Override
    protected String getConfigResources() {
        return "stripe-config.xml";
    }
    
    @Before
    public void setUp() throws Exception {
    	params.put("zero", 0);
    	params.put("one", 1);
    	params.put("null", null);
    	params.put("valid", "valid");
    }
    
    @Test
    public void testRemoveOptionalsAndZeroes() throws Exception {
    	Map<String, Object> result = StripeClientUtils.removeOptionalsAndZeroes(params);
    	assertEquals(params.get("valid"), result.get("valid"));
    	assertNull(result.get("zero"));
    	assertNull(result.get("null"));
    	assertEquals(params.get("one"), result.get("one"));
    }
    
    @Test
    public void testRemoveOptionals() throws Exception {
    	Map<String, Object> result = StripeClientUtils.removeOptionals(params);
    	assertEquals(params.get("valid"), result.get("valid"));
    	assertEquals(params.get("zero"), result.get("zero"));
    	assertNull(result.get("null"));
    	assertEquals(params.get("one"), result.get("one"));
    }
    
}
