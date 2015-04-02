
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.testcases.DeleteCustomerTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveCustomerTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateCustomerTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
    RetrieveCustomerTestCases.class,
    UpdateCustomerTestCases.class,
    DeleteCustomerTestCases.class
})
public class SmokeTestSuite {


}
