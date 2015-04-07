
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.testcases.DeleteCardTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllCustomerCardsTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveCardTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateCardTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
    RetrieveCardTestCases.class,
    UpdateCardTestCases.class,
    DeleteCardTestCases.class,
    ListAllCustomerCardsTestCases.class,   
})
public class SmokeTestSuite {


}
