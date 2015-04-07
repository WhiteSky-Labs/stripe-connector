
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.testcases.CaptureChargeTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateChargeTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllChargesTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveChargeTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateChargeTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
    CreateChargeTestCases.class,
    RetrieveChargeTestCases.class,
    UpdateChargeTestCases.class,
    CaptureChargeTestCases.class,
    ListAllChargesTestCases.class
})
public class SmokeTestSuite {


}
