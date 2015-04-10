/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */


package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.SmokeTests;
import com.wsl.modules.stripe.automation.testcases.CancelSubscriptionTestCases;
import com.wsl.modules.stripe.automation.testcases.CaptureChargeTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateAccountTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateCardTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateChargeTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateCouponTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateCustomerTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateInvoiceTestCases;
import com.wsl.modules.stripe.automation.testcases.CreatePlanTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateRefundTestCases;
import com.wsl.modules.stripe.automation.testcases.CreateSubscriptionTestCases;
import com.wsl.modules.stripe.automation.testcases.DeleteCardTestCases;
import com.wsl.modules.stripe.automation.testcases.DeleteCouponTestCases;
import com.wsl.modules.stripe.automation.testcases.DeleteCustomerTestCases;
import com.wsl.modules.stripe.automation.testcases.DeletePlanTestCases;
import com.wsl.modules.stripe.automation.testcases.ListActiveSubscriptionsTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllApplicationFeesTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllBalanceHistoryTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllChargesTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllCouponsTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllCustomerCardsTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllCustomersTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllPlansTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllRefundsTestCases;
import com.wsl.modules.stripe.automation.testcases.PayInvoiceTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveAccountTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveAllInvoicesTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveApplicationFeeTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveBalanceTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveBalanceTransactionTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveCardTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveChargeTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveCouponTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveCustomerTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveInvoiceLineItemsTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveInvoiceTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrievePlanTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveRefundTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveSubscriptionTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveUpcomingInvoiceTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateAccountTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateCardTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateChargeTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateCouponTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateCustomerTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateInvoiceTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdatePlanTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateRefundTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateSubscriptionTestCases;

import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
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
public class SmokeTestSuite {


}
