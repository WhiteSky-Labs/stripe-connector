
package com.wsl.modules.stripe.automation.testcases;

import static org.junit.Assert.assertNotNull;

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

}
