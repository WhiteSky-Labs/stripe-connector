
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.testcases.CreateRefundTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllRefundsTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveRefundTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateRefundTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(RegressionTests.class)
@org.junit.runners.Suite.SuiteClasses({
    CreateRefundTestCases.class,
    RetrieveRefundTestCases.class,
    UpdateRefundTestCases.class,
    ListAllRefundsTestCases.class
})
public class RegressionTestSuite {


}
