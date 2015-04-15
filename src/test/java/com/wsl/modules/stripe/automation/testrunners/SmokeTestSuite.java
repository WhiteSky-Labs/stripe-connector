/**
 *
 * (c) 2015 WhiteSky Labs, Pty Ltd. This software is protected under international
 * copyright law. All use of this software is subject to WhiteSky Labs' Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and WhiteSky Labs. If such an agreement is not in
 * place, you may not use the software.
 */


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
