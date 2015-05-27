---
layout: post
title: Stripe Connector v1.0.0
---

**Premium Partner Connector**

The Anypoint Connector for Stripe provides the ability to perform most common tasks against the Stripe API in an easy, consistent way. It allows you to easily accept payments, create charges, manage accounts using Stripe Connect, manage subscriptions and Invoices, and perform refunds.

# Introduction

The Stripe Connector facilitates connections between Mule integration applications and Stripe via its Java SDK. It is a Premium Connector, meaning that you can deploy it into production if you have an Mule Enterprise License or a CloudHub account, as well as a license to use the Connector from WhiteSky Labs, Pty Ltd.

The Stripe Connector provides access to over 60 APIs in Stripe. It allows you to perform a large set of actions including managing Accounts, Subscriptions, Balances, Coupons, Charges, Cards and all forms of payments and payment management.

## Assumptions

This document assumes that you are familiar with Mule, Anypoint™ Connectors, and the Anypoint Studio interface. To increase your familiarity with Studio, consider completing one or more Anypoint Studio Tutorials. Further, this page assumes that you have a basic understanding of Mule flows and Mule Global Elements.

This document describes implementation examples within the context of Anypoint Studio, Mule ESB’s graphical user interface, and, in parallel, includes configuration details for doing the same in the XML Editor.

## Requirements

There are no hardware, software or technical requirements in addition to those required to run the AnyPoint Platform.

You will require a valid CloudHub Subcription or Mule ESB Enterprise License in order to use the Connector, as well as a valid agreement with WhiteSky Labs to use the Stripe Connector.

## Dependencies

The Stripe Connector requires AnyPoint Studio 5.1.1 or greater with Mule EE ESB Runtime 3.6.0 or newer.

### Versions

The Connector was developed and tested using ESB 3.6.0 and AnyPoint Studio 5.1.1, using the Stripe SDK for Java version 1.27.0.

### Consumers

The Connector requires the user to have a valid Stripe account.

## Compatibility Matrix

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

# Installing and Configuring

You can "test drive" the Stripe Connector by installing it in Anypoint Studio. Follow the instructions to download and launch Anypoint Studio, then follow the steps below to install the Stripe Connector.

However, to use the Stripe Connector in a production environment, you must have either:

* an Enterprise license to use Mule
* a CloudHub Starter, Professional, or Enterprise account
* Contact the MuleSoft Sales Team to obtain either of these.

## Installing

To install the Stripe Connector:

1. Under the Help menu in Anypoint Studio, select Install New Software.
2. Next to the Work with: field of the Install wizard, choose the "Add:" button. Choose "Archive" and navigate to the installation zip file provided to you by WhiteSky Labs. Finally, give the site a name (e.g. "Stripe Connector Installer") and choose OK.
3. In the table below the Filter field (see image below), click to expand the Standard folder, then select Mule Stripe Connector. Click Next.
4. Review the details, then click *Next*.
5. Click to accept the terms and conditions, then click *Finish*.
6. Click *Restart Now* to complete the installation. Once you have installed the connector and restarted Studio you will see the Stripe Connector available in the palette, under the Connectors category.

    ![Stripe Connector Palette]({{ site.url }}/{{ site.baseurl }}/images/stripepalette.png)

## Configuring

To use the Stripe Connector, you must configure two things:

* an instance of the connector in your application
* a global Stripe connector configuration

To create a global Stripe connector configuration, follow these steps:

1. Click the Global Elements tab at the base of the canvas, then click Create.
2. Use the filter to locate, then select Stripe, then click OK.
3. Configure the global connector's parameters according to the table below.

    ![Stripe Global Parameters]({{ site.url }}/{{ site.baseurl }}/images/createstripeconfigref.png)

    <table>
    <tr>
        <th>Parameter</th>
        <th>Description</th>
        <th>Example</th>
    </tr>
    <tr><td>Name</td><td>Name for the global element</td><td>Splunk</td></tr>
    <tr><td>ApiVersion</td><td>(Optional) Stripe API Version</td><td>2015-03-24</td></tr>
    <tr><td>ApiKey</td><td>Your Stripe API Key</td><td>sk_abc123</td></tr>
    </table>

4. Access the Pooling Profile tab to configure any settings relevant to managing multiple connections via a connection pool.
5. Access the Reconnection tab to configure any settings relevant to reconnection strategies that Mule should execute if it loses its connection to Stripe.
6. Click Test Connection to confirm that the parameters of your global Stripe connector are accurate, and that Mule is able to successfully connect to your instance of Stripe.
7. Click OK to save the global connector configurations.
8. Return to the Message Flow tab in Studio. Drag the Stripe Connector onto the canvas, then select it to open the Properties Editor console.
9. Configure the connector's parameters according to the table below.

    ![Stripe Connector Instance Parameters]({{ site.url }}/{{ site.baseurl }}/images/setstripeprops.png)

    <table>
    <tr>
        <th>Field</th>
        <th>Description</th>
        <th>Default</th>
    </tr>
    <tr><td>Display Name</td><td>A human-readable name for the Connector operation</td><td>Retrieve Customer</td></tr>
    <tr><td>Config Reference</td><td>The global configuration for the Connector to connect to Stripe</td><td>Stripe__Connection_Management_Strategy</td></tr>
    <tr><td>Operation</td><td>The operation to perform.</td><td>Retrieve Customer</td></tr>
    <tr><td>id</td><td>A parameter for the operation chosen.</td><td>#[payload]</td></tr>
    </table>

10. Click blank space on the canvas to save your Stripe connector configurations.

## Example Use Case

It's time to build the flows which creates a Customer, retrieves them, and deletes the Customer.

![Create Customer flow]({{ site.url }}/{{ site.baseurl }}/images/createcustomer.png)

![Retrieve and Delete flows]({{ site.url }}/{{ site.baseurl }}/images/retrieveandremovecustomer.png)

**Create Customer flow:** This is the flow which creates a Customer in Stripe. Start by dragging an HTTP endpoint from the palette onto the flow. Create a new Connector Configuration for this endpoint and accept the defaults.

![HTTP Connector Configuration]({{ site.url }}/{{ site.baseurl }}/images/httpconnectorconfiguration.png)

Now add a path to your HTTP receiver: "/createcustomer". This is the URL you will call to start the flow.

![HTTP Receiver Configuration]({{ site.url }}/{{ site.baseurl }}/images/httpreceiverconfiguration.png)


Then drag a Stripe Connector onto the flow after the HTTP endpoint. In the configuration window for the Stripe Connector, select the previously created Stripe config from the Config Reference dropdown. Set the Operation to "Create Customer", and provide an email and description. Add an "Object to JSON" transformer at the end to make sure the response is readable by a human. Click okay.

![Create Customer Flow]({{ site.url }}/{{ site.baseurl }}/images/createcustomerdetail.png)

This completes the Create Customer flow.

**Retrieve Customer flow:** This is the flow which retrieves the Customer. Start by dragging an HTTP endpoint from the palette onto the workspace (not onto a flow), creating a new flow. Use the existing HTTP configuration. Configure the Path to "/retrieveCustomer". This is the URL you will call to start the flow.
Then drag a Stripe Connector onto the flow after the HTTP endpoint. In the configuration window for the Stripe Connector, select the previously created Stripe config from the Config Reference dropdown. Set the Operation to "Retrieve Customer", and set the id field to "#[message.inboundProperties.'http.query.params'.id]". Add an "Object to JSON" transformer at the end to make sure the response is readable by a human. Click OK.

![Retrieve Customer Flow]({{ site.url }}/{{ site.baseurl }}/images/retrievecustomerflow.png)


**Delete Customer Flow:** This is the flow which removes the customer you created. Start by dragging an HTTP endpoint from the palette onto the workspace (not onto a flow), creating a new flow. Use the existing HTTP configuration. Configure the Path to "/deleteCustomer". This is the URL you will call to start the flow.
Then drag a Stripe Connector onto the flow after the HTTP endpoint. In the configuration window for the Stripe Connector, select the previously created Stripe config from the Config Reference dropdown. Set the Operation to "Delete Customer", and set the id field to "#[message.inboundProperties.'http.query.params'.id]". Add an "Object to JSON" transformer at the end to make sure the response is readable by a human. Click OK.

![Delete Customer Flow]({{ site.url }}/{{ site.baseurl }}/images/deletecustomer.png)

**Flow XML**

The final flow XML should look like this.

	<?xml version="1.0" encoding="UTF-8"?>

<mule xmlns:json="http://www.mulesoft.org/schema/mule/json" xmlns:http="http://www.mulesoft.org/schema/mule/http" xmlns:tracking="http://www.mulesoft.org/schema/mule/ee/tracking" xmlns:stripe="http://www.mulesoft.org/schema/mule/stripe" xmlns="http://www.mulesoft.org/schema/mule/core" xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
	xmlns:spring="http://www.springframework.org/schema/beans" version="EE-3.6.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-current.xsd
http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
http://www.mulesoft.org/schema/mule/stripe http://www.mulesoft.org/schema/mule/stripe/current/mule-stripe.xsd
http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
http://www.mulesoft.org/schema/mule/ee/tracking http://www.mulesoft.org/schema/mule/ee/tracking/current/mule-tracking-ee.xsd
http://www.mulesoft.org/schema/mule/json http://www.mulesoft.org/schema/mule/json/current/mule-json.xsd">
    <stripe:config-type name="Stripe__Connection_Management_type_strategy" apiKey="<YOUR_API_KEY>" doc:name="Stripe: Connection Management type strategy"/>
    <http:listener-config name="HTTP_Listener_Configuration" host="0.0.0.0" port="8081" doc:name="HTTP Listener Configuration"/>
    <flow name="stripe-createCustomerFlow">
        <http:listener config-ref="HTTP_Listener_Configuration" path="/createcustomer" doc:name="HTTP"/>
        <stripe:create-customer config-ref="Stripe__Connection_Management_type_strategy" description="Test Customer" email="demo@demo.com" doc:name="CreateCustomer"/>
        <json:object-to-json-transformer doc:name="Object to JSON"/>
    </flow>
    <flow name="stripe-retrieveCustomerFlow">
        <http:listener config-ref="HTTP_Listener_Configuration" path="/retrievecustomer" doc:name="HTTP"/>
        <stripe:retrieve-customer config-ref="Stripe__Connection_Management_type_strategy" id="#[message.inboundProperties.'http.query.params'.id]" doc:name="RetrieveCustomer"/>
        <json:object-to-json-transformer doc:name="Object to JSON"/>
    </flow>
    <flow name="stripe-deleteCustomerFlow">
        <http:listener config-ref="HTTP_Listener_Configuration" path="/deletecustomer" doc:name="HTTP"/>
        <stripe:delete-customer config-ref="Stripe__Connection_Management_type_strategy" id="#[message.inboundProperties.'http.query.params'.id]" doc:name="Delete Customer"/>
        <json:object-to-json-transformer doc:name="Object to JSON"/>
    </flow>
</mule>



**Testing the app**

Now it's time to test the app. Run the app in Anypoint Studio and open a browser window. Visit [http://localhost:8081/createcustomer](http://localhost:8081/createcustomer). This will create a Customer in Stripe and return the ID in the JSON.
Now visit [http://localhost:8081/retrievecustomer?id=<ID RETRIEVED FROM CREATE CALL>](http://localhost:8081/retrievecustomer?id=<ID RETRIEVED FROM CREATE CALL>). This will retrieve the customer, confirming it exists.
Now visit [http://localhost:8081/deletecustomer?id=<ID RETRIEVED FROM CREATE CALL>](http://localhost:8081/deletecustomer?id=<ID RETRIEVED FROM CREATE CALL>). This will remove the previously created customer.

## Updating From an Older Version

There is currently no procedure for upgrading as only one version of the Connector exists.

# Using This Connector

## Best Practices

* It is important to understand how the Stripe API works in order to effectively use the Connector, especially the relationships between accounts, application fees and Stripe Connect. Review the Stripe API documentation to learn more.

# Go Further

* [Release Notes](http://whitesky-labs.github.io/stripe-connector/2015/04/15/release-notes/)
* [Stripe SDK for Java Documentation](https://stripe.com/docs/api/java)
* API Documentation is available at [http://whitesky-labs.github.io/stripe-connector/apidocs](http://whitesky-labs.github.io/stripe-connector/apidocs)
* Read more about [http://www.mulesoft.org/documentation/display/current/Anypoint+Connectors](AnyPoint Connectors)