
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.testcases.CancelSubscriptionTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateSubscriptionTestCases;
import com.wsl.modules.stripe.automation.testcases.ListActiveSubscriptionsTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveSubscriptionTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateSubscriptionTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
    CreateSubscriptionTestCases.class,
    RetrieveSubscriptionTestCases.class,
    UpdateSubscriptionTestCases.class,
    CancelSubscriptionTestCases.class,
    ListActiveSubscriptionsTestCases.class
})
public class SmokeTestSuite {


}
