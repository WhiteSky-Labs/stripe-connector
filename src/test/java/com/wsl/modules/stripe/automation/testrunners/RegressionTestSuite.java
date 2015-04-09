
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.testcases.RetrieveUpcomingInvoiceTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(RegressionTests.class)
@org.junit.runners.Suite.SuiteClasses({
    RetrieveUpcomingInvoiceTestCases.class
})
public class RegressionTestSuite {


}
