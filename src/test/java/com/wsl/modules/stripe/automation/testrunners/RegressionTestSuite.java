
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.testcases.*;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(RegressionTests.class)
@org.junit.runners.Suite.SuiteClasses({
    CreateFileUploadTestCases.class,
    CreateBitcoinReceiverTestCases.class,
    RetrieveBitcoinReceiverTestCases.class,
    ListAllBitcoinReceiversTestCases.class,
    CreateApplicationFeeRefundTestCases.class,
    RetrieveApplicationFeeRefundTestCases.class,
    UpdateApplicationFeeRefundTestCases.class,
    ListAllApplicationFeeRefundsTestCases.class,
    RetrieveEventTestCases.class,
    ListAllEventsTestCases.class,
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
