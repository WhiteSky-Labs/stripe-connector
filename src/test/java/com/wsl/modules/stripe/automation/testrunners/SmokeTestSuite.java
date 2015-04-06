
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.testcases.ListAllBalanceHistoryTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveBalanceTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveBalanceTransactionTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
    RetrieveBalanceTestCases.class,
    RetrieveBalanceTransactionTestCases.class,
    ListAllBalanceHistoryTestCases.class
})
public class SmokeTestSuite {


}
