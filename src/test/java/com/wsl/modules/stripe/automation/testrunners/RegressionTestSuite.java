/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.testcases.*;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(RegressionTests.class)
@org.junit.runners.Suite.SuiteClasses({
    CreateCardTokenTestCases.class,
    CreateBankAccountTokenTestCases.class,
    RetrieveTokenTestCases.class,
	CancelSubscriptionTestCases.class,
	RetrieveAccountTestCases.class,
	CaptureChargeTestCases.class,
	RetrieveAllInvoicesTestCases.class,
	CreateAccountTestCases.class,
	RetrieveApplicationFeeTestCases.class,
	CreateCardTestCases.class,
	RetrieveBalanceTestCases.class,
	CreateChargeTestCases.class,
	RetrieveBalanceTransactionTestCases.class,
	CreateCouponTestCases.class,
	RetrieveCardTestCases.class,
	CreateCustomerTestCases.class,
	RetrieveChargeTestCases.class,
	CreateInvoiceTestCases.class,
	RetrieveCouponTestCases.class,
	CreatePlanTestCases.class,
	RetrieveCustomerTestCases.class,
	CreateRefundTestCases.class,
	RetrieveInvoiceLineItemsTestCases.class,
	CreateSubscriptionTestCases.class,
	RetrieveInvoiceTestCases.class,
	DeleteCardTestCases.class,
	RetrievePlanTestCases.class,
	DeleteCouponTestCases.class,
	RetrieveRefundTestCases.class,
	DeleteCustomerTestCases.class,
	RetrieveSubscriptionTestCases.class,
	DeletePlanTestCases.class,
	RetrieveUpcomingInvoiceTestCases.class,
	ListActiveSubscriptionsTestCases.class,
	UpdateAccountTestCases.class,
	ListAllApplicationFeesTestCases.class,
	UpdateCardTestCases.class,
	ListAllBalanceHistoryTestCases.class,
	UpdateChargeTestCases.class,
	ListAllChargesTestCases.class,
	UpdateCouponTestCases.class,
	ListAllCouponsTestCases.class,
	UpdateCustomerTestCases.class,
	ListAllCustomerCardsTestCases.class,
	UpdateInvoiceTestCases.class,
	ListAllCustomersTestCases.class,
	UpdatePlanTestCases.class,
	ListAllPlansTestCases.class,
	UpdateRefundTestCases.class,
	ListAllRefundsTestCases.class,
	UpdateSubscriptionTestCases.class,
	PayInvoiceTestCases.class
})
public class RegressionTestSuite {


}
