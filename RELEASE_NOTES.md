---
title: Anypoint Connector for Stripe Release Notes
layout: post
---

AnyPoint Connector for Stripe Release Notes
=====================================

Date: 14-April-2015

Version: 1.0.0

Supported Mule Runtime Versions: 3.6.x

Supported API versions
----------------------
Stripe API 2015-03-24


New Features and Functionality
------------------------------
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