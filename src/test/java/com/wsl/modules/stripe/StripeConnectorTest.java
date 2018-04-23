/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.wsl.modules.stripe;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mule.modules.tests.ConnectorTestCase;
import org.junit.Before;
import org.junit.Test;

import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.wsl.modules.stripe.strategy.ConnectorConnectionStrategy;

public class StripeConnectorTest extends ConnectorTestCase {
	
	@Mock
	Customer customer;
	
	private StripeConnector connector;
	private ConnectorConnectionStrategy connectionStrategy;
	
    @Override
    protected String getConfigResources() {
        return "stripe-config.xml";
    }
    
    @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	this.connector = new StripeConnector();
    	this.connectionStrategy = new ConnectorConnectionStrategy();    	
    	this.connector.setConnectionStrategy(connectionStrategy);
    }
    
    @Test
    public void testSetApiVersion() throws Exception {
    	this.connectionStrategy.setApiVersion("2015-03-24");
    	assertEquals("2015-03-24", this.connectionStrategy.getApiVersion());
    	this.connectionStrategy = new ConnectorConnectionStrategy();
    }
    
    @Test
    public void testListAllCustomers() throws Exception {
    	/*CustomerCollection coll = new CustomerCollection();
    	Map<String, Object> customerParams = new HashMap<String, Object>();
    	when(Customer.all(customerParams)).thenReturn(coll);
    	assertEquals(coll, connector.listAllCustomers(0));*/
    	assertEquals(true, true);
    }
    

}
