
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.stripe.model.Card;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.DeletedCard;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class DeleteCardTestCases
    extends StripeTestParent
{
	private String cardId;
	private String customerId;

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
        initializeTestRunMessage("deleteCardTestData");
        upsertOnTestRunMessage("id", this.cardId);
        upsertOnTestRunMessage("ownerId", this.customerId);
    }

    @After
    public void tearDown()
        throws Exception
    {
    	try {
    		initializeTestRunMessage("retrieveCardTestData");
    		upsertOnTestRunMessage("id", this.cardId);
    		upsertOnTestRunMessage("ownerId", this.customerId);
    		Object result = runFlowAndGetPayload("retrieve-card");
            Card card = (Card)result;
            if (card != null) {
            	initializeTestRunMessage("deleteCardTestData");
            	upsertOnTestRunMessage("id", this.cardId);
            	upsertOnTestRunMessage("ownerId", this.customerId);
            	runFlowAndGetPayload("delete-card");
            }
            initializeTestRunMessage("deleteCustomerTestData");
            upsertOnTestRunMessage("id", this.customerId);
            runFlowAndGetPayload("delete-customer");
    	} catch (MessagingException e){
    		// this is expected, if the card doesn't exist.
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeleteCard()
        throws Exception
    {
        Object result = runFlowAndGetPayload("delete-card");        
        DeletedCard card = (DeletedCard) result;
        assertTrue(card.getDeleted().booleanValue());
        // Confirm the card doesn't exist
        initializeTestRunMessage("retrieveCardTestData");
        upsertOnTestRunMessage("id", this.cardId);
        upsertOnTestRunMessage("ownerId", this.customerId);
        
        try {
    		runFlowAndGetPayload("retrieve-card");
    		fail("Retrieving a card that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not retrieve the Card"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testDeleteCardThatDoesntExist()
        throws Exception
    {
        upsertOnTestRunMessage("id", "InvalidID");
        upsertOnTestRunMessage("ownerId", this.customerId);
    	try {
    		runFlowAndGetPayload("delete-card");
    		fail("Deleting a card that doesn't exist should throw an error.");
    	} catch (MessagingException e){
    		assertTrue(e.getCause().getMessage().contains("Could not delete the Card"));
    	} catch (Exception e){
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}
        
    }

}
