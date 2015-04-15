---
layout: post
title: Stripe Connector 1.0.0 Release Notes
redirect_from: "/release-notes/"
---


**Premium Partner Connector**

The Anypoint Connector for Stripe provides the ability to perform most common tasks against the Stripe API in an easy, consistent way. It allows you to easily accept payments, create charges, manage accounts using Stripe Connect, manage subscriptions and Invoices, and perform refunds.

This is the first release version of the Stripe Connector, with comprehensive support for most APIs out of the box.

## Compatibility

<table>
    <tr>
        <th>Application/Service</th>
        <th>Version</th>
    </tr>
    <tr>
        <td>Mule Runtime</td>
        <td>Mule 3.6.0 and above</td>
    </tr>
    <tr>
        <td>Stripe API Version</td>
        <td>2015-03-24</td>
    </tr>
    <tr>
        <td>Stripe SDK for Java</td>
        <td>v1.27.1</td>
    </tr>
</table>

## Supported Stripe Operations

Supported Operations:

* Connect to Stripe
* Charges
  * Create a Charge
  * Retrieve a Charge
  * Update a Charge
  * Capture a Charge
  * List all Charges
* Refunds
  * Create a Refund
  * Retrieve a Refund
  * Update a Refund
  * List All Refunds
* Customer
  * Create a Customer
  * Retrieve a Customer
  * Update a Customer
  * Delete a Customer
  * List all Customers
* Cards
  * Create a Card
  * Retrieve a Card
  * Update a Card
  * Delete a Card
  * List all Cards
* Subscriptions
  * Create a Subscription
  * Retrieve a Subscription
  * Update a Subscription
  * Cancel a Subscription
  * List active Subscriptions
* Plans
  * Create a Plan
  * Retrieve a Plan
  * Update a Plan
  * Delete a Plan
  * List all Plans
* Coupons
  * Create a Coupon
  * Retrieve a Coupon
  * Update a Coupon
  * Delete a Coupon
  * List all Coupons
* Invoices
  * Create an Invoice
  * Retrieve an Invoice
  * Retrieve an Invoice's Line Items
  * Retrieve an Upcoming Invoice
  * Update an Invoice
  * Pay an Invoice
  * List all Invoices
* Application Fees
  * Retrieve an Application Fee
  * List all Application Fees
* Application Fee Refunds
  * Create an Application Fee Refund
  * Retrieve an Application Fee Refund
  * Update an Application Fee Refund
  * List all Application Fee Refunds
* Accounts
  * Create an Account
  * Retrieve Account Details
  * Update an Account
  * List all Connected Accounts
* Balance
  * Retrieve Balance
  * Retrieve a Balance Transaction
  * List all Balance History
* Events
  * List all Events
  * Retrieve an Event
  * List all Events
* Tokens
  * Create a Card Token
  * Create a Bank Account Token
  * Retrieve a Token
* Bitcoin Receivers
  * Create a Receiver
  * Retrieve a Receiver
  * List all Receivers
* File Uploads
  * Create a File Upload

Unsupported APIs are due to deprecation of the Transfers system by Stripe, or bugs in the API endpoints at time of development.
* Discounts are not supported.
* Disputes are not supported, as they are better dealt with in the Stripe Dashboard.
* Transfers are deprecated for Stripe Connect
* Transfer Requests are deprecated for Stripe Connect
* Retrieve and List File Uploads does not currently work due to an issue in the Stripe API.



Closed Issues in this release
-----------------------------

* First Release

Known Issues in this release
----------------------------

* None identified

## Support Resources

* [Overview Documentation](http://whitesky-labs.github.io/stripe-connector/2015/04/15/stripe-connector/)
* [Stripe SDK for Java Documentation](https://stripe.com/docs/api/java)
* API Documentation is available at [http://whitesky-labs.github.io/stripe-connector/apidocs](http://whitesky-labs.github.io/stripe-connector/apidocs)
* You can report new issues by emailing [support@whiteskylabs.com](mailto:support@whiteskylabs.com).
