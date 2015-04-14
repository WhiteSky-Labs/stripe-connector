package com.wsl.modules.stripe;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mule.modules.tests.ConnectorTestCase;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;
import com.stripe.model.Subscription;
import com.wsl.modules.stripe.client.StripeSubscriptionClient;
public class StripeSubscriptionClientTest extends ConnectorTestCase {
	@Mock
	Customer customer;
	
	@Mock 
	CustomerSubscriptionCollection coll;
	
	@Mock
	Subscription subscription;
	
	@Spy
	StripeSubscriptionClient subClient = new StripeSubscriptionClient();
	
	@Override
    protected String getConfigResources() {
        return "stripe-config.xml";
    }
    
    @Before
    public void setUp() throws Exception {
    	MockitoAnnotations.initMocks(this);
    	Stripe.apiKey = "sk_test_WOw8pJhJyOIBgveU9C8lx79l";
    	this.subClient = Mockito.spy(new StripeSubscriptionClient());
    }
    
    @Test
    public void testCreateSubscription() throws Exception {
    	/*Customer customer = new Customer();
    	customer.setId("test");
    	when(Customer.retrieve("test")).thenReturn(customer);
    	Subscription sub = new Subscription();
    	sub.setId("test");
    	when(customer.createSubscription(any(Map.class))).thenReturn(new Subscription());
    	Subscription result = subClient.createSubscription("test", "testplan", "test", null, "test", null, 0, 0, 0, null);
    	assertEquals("test", result.getId());
    	assertEquals("testplan", result.getPlan());
    	
    	*/
    }
    
}
