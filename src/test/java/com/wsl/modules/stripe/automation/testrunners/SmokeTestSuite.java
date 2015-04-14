
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.testcases.ListAllCustomersTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveAccountTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveBalanceTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveCustomerTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveEventTestCases;

import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
	RetrieveEventTestCases.class,
    RetrieveAccountTestCases.class,
	RetrieveBalanceTestCases.class,
	RetrieveCustomerTestCases.class,
	ListAllCustomersTestCases.class	 
})
public class SmokeTestSuite {
	
}
