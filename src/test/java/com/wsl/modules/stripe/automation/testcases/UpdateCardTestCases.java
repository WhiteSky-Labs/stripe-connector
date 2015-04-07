
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class UpdateCardTestCases
    extends StripeTestParent
{

	private String customerId;
	private String cardId;

    @Before
    public void setup()
        throws Exception
    {
    	initializeTestRunMessage("createCustomerTestData");
    	Object result = runFlowAndGetPayload("create-customer");
        Customer customer = (Customer) result;
        this.customerId = customer.getId();
        initializeTestRunMessage("createCardTestData");
        upsertOnTestRunMessage("ownerId", customerId);
        result = runFlowAndGetPayload("create-card");
        Card card = (Card) result;
        this.cardId = card.getId();
        
        initializeTestRunMessage("updateCardTestData");
        upsertOnTestRunMessage("id", this.cardId);
        upsertOnTestRunMessage("ownerId", this.customerId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	initializeTestRunMessage("deleteCardTestData");
    	upsertOnTestRunMessage("id", this.cardId);
    	upsertOnTestRunMessage("ownerId", this.customerId);
    	runFlowAndGetPayload("delete-card");
	    initializeTestRunMessage("deleteCustomerTestData");
	    upsertOnTestRunMessage("id", this.customerId);
	    runFlowAndGetPayload("delete-customer");
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testUpdateCard()
        throws Exception
    {
        Object result = runFlowAndGetPayload("update-card");
        Map<String, String> expectedBean = getBeanFromContext("updateCardTestData");
        Card card = (Card)result;        
        assertEquals(expectedBean.get("addressCity"), card.getAddressCity());
        assertEquals(expectedBean.get("addressCountry"), card.getAddressCountry());
        assertEquals(expectedBean.get("cardName"), card.getName());
    }

}
