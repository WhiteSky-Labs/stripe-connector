
package com.wsl.modules.stripe.automation.testrunners;

import com.wsl.modules.stripe.automation.RegressionTests;
import com.wsl.modules.stripe.automation.testcases.CreateCouponTestCases;
import com.wsl.modules.stripe.automation.testcases.DeleteCouponTestCases;
import com.wsl.modules.stripe.automation.testcases.ListAllCouponsTestCases;
import com.wsl.modules.stripe.automation.testcases.RetrieveCouponTestCases;
import com.wsl.modules.stripe.automation.testcases.UpdateCouponTestCases;
import org.junit.runner.RunWith;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(RegressionTests.class)
@org.junit.runners.Suite.SuiteClasses({
    CreateCouponTestCases.class,
    RetrieveCouponTestCases.class,
    UpdateCouponTestCases.class,
    DeleteCouponTestCases.class,
    ListAllCouponsTestCases.class
})
public class RegressionTestSuite {


}
