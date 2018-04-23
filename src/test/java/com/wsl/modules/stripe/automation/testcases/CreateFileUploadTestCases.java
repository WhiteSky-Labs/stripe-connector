/**
 * (c) 2018 WhiteSky Labs, Pty Ltd. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;

import com.stripe.model.FileUpload;
import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.StripeTestParent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.Timeout;
import org.mule.api.MessagingException;
import org.mule.modules.tests.ConnectorTestUtils;

public class CreateFileUploadTestCases
    extends StripeTestParent
{
    @Rule
    public Timeout globalTimeout = new Timeout(200000);


    @Before
    public void setup()
        throws Exception
    {
        initializeTestRunMessage("createFileUploadTestData");        
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateFileUpload()
        throws Exception
    {
        Object result = runFlowAndGetPayload("create-file-upload");
        assertNotNull(result);
        FileUpload upload = (FileUpload) result;
        assertNotNull(upload.getId());
    }
    
        @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateFileUploadForDispute()
        throws Exception
    {
        	upsertOnTestRunMessage("purpose", "DISPUTE_EVIDENCE");
        Object result = runFlowAndGetPayload("create-file-upload");
        assertNotNull(result);
        FileUpload upload = (FileUpload) result;
        assertNotNull(upload.getId());
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateInvalidFileUpload()
        throws Exception
    {
    	try{
    		upsertOnTestRunMessage("file", "missingfile.gone");
    		Object result = runFlowAndGetPayload("create-file-upload");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("Could not create the File Upload"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }
    
    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testCreateInvalidTypeFileUpload()
        throws Exception
    {
    	try{
    		upsertOnTestRunMessage("purpose", "missingfile.gone");    		
    		Object result = runFlowAndGetPayload("create-file-upload");
    		fail("Error should be thrown");
    	} catch (MessagingException e) {
    		assertTrue(e.getCause().getMessage().contains("No enum constant"));
    	} catch (Exception e) {
    		fail(ConnectorTestUtils.getStackTrace(e));
    	}    	
    }

}
