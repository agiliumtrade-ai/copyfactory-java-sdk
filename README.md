# CopyFactory trade copying API for Java (a member of [metaapi.cloud](https://metaapi.cloud) project)

CopyFactory is a powerful copy trading API which makes developing forex trade copying applications as easy as writing few lines of code.

CopyFactory API is a member of MetaApi project ([https://metaapi.cloud](https://metaapi.cloud)), a powerful cloud forex trading API which supports both MetaTrader 4 and MetaTrader 5 platforms.

MetaApi is a paid service, however API access to one MetaTrader account is free of charge.

The [MetaApi pricing](https://metaapi.cloud/#pricing) was developed with the intent to make your charges less or equal to what you would have to pay
for hosting your own infrastructure. This is possible because over time we managed to heavily optimize
our MetaTrader infrastructure. And with MetaApi you can save significantly on application development and
maintenance costs and time thanks to high-quality API, open-source SDKs and convenience of a cloud service.

## Why do we offer CopyFactory trade copying API

We found that developing reliable and flexible trade copier is a task
which requires lots of effort, because developers have to solve a series
of complex technical tasks to create a product.

We decided to share our product as it allows developers to start with a
powerful solution in almost no time, saving on development and
infrastructure maintenance costs.

## Frequently asked questions (FAQ)
FAQ is located here: [http://metaapi.cloud/docs/copyfactory/faq](http://metaapi.cloud/docs/copyfactory/faq)

## CopyFactory copy tradging API features

Features supported:

- low latency trade copying API
- reliable trade copying API
- suitable for large-scale deployments
- suitable for large number of subscribers
- connect arbitrary number of strategy providers and subscribers
- subscribe accounts to multiple strategies at once
- select arbitrary copy ratio for each subscription
- configure symbol mapping between strategy providers and subscribers
- apply advanced risk filters on strategy provider side
- override risk filters on subscriber side
- provide multiple strategies from a single account based on magic or symbol filters
- supports manual trading on subscriber accounts while copying trades
- synchronize subscriber account with strategy providers
- monitor trading history
- calculate trade copying commissions for account managers
- support portfolio strategies as trading signal source, i.e. the strategies which include signals of several other strategies (also known as combos on some platforms)

Please note that trade copying to MT5 netting accounts is not supported in the current API version

## REST API documentation
CopyFactory SDK is built on top of CopyFactory REST API.

CopyFactory REST API docs are available at [https://metaapi.cloud/docs/copyfactory/](https://metaapi.cloud/docs/copyfactory/)

## Installation
If you use Apache Maven, add this to `<dependencies>` in your `pom.xml`:
```xml
<dependency>
  <groupId>cloud.metaapi.sdk</groupId>
  <artifactId>copyfactory-java-sdk</artifactId>
  <version>1.1.0</version>
</dependency>
```

## Code examples
We published some code examples in our github repository, namely:

- Java: [https://github.com/agiliumtrade-ai/copyfactory-java-sdk/tree/master/examples](https://github.com/agiliumtrade-ai/copyfactory-java-sdk/tree/master/examples)

Other options can be found on [this page](https://search.maven.org/artifact/cloud.metaapi.sdk/copyfactory-java-sdk/1.1.0/jar).

### Running Java SDK examples
In order to run Java SDK examples, follow these steps:
1. Make sure that you have installed [Maven](http://maven.apache.org) and its command `mvn` is accessible.
2. Navigate to the root folder of the example project (where its `pom.xml` is located).
3. Build the project with `mvn package`.
4. Run `mvn exec:java@`_`ExampleClassName`_ where _`ExampleClassName`_ is the example to execute, e.g. `mvn exec:java@CopyFactoryExample`.

Example parameters such as token or account id can be passed via environment variables, or set directly in the example source code. In the last case you need to rebuild the example with `mvn package`. 

## Retrieving API token
Please visit [https://app.metaapi.cloud/token](https://app.metaapi.cloud/token) web UI to obtain your API token.

### Configuring trade copying

In order to configure trade copying you need to:

- add MetaApi MetaTrader accounts with CopyFactory as application field value (see above)
- create CopyFactory master and slave accounts and connect them to MetaApi accounts via connectionId field
- create a strategy being copied
- subscribe slave CopyFactory accounts to the strategy

```java
import cloud.metaapi.sdk.metaApi.MetaApi;
import cloud.metaapi.sdk.copyFactory.CopyFactory;

String token = "...";
MetaApi metaapi = new MetaApi(token);
CopyFactory copyFactory = new CopyFactory(token);

// retrieve MetaApi MetaTrader accounts with CopyFactory as application field value
MetatraderAccount masterMetaapiAccount = metaapi.getMetatraderAccountApi().getAccount("masterMetaapiAccountId").get();
if (!masterMetaapiAccount.getApplication().equals("CopyFactory")) {
  throw new Exception("Please specify CopyFactory application field value in your MetaApi account in order to use it in CopyFactory API");
}
MetatraderAccount slaveMetaapiAccount = metaapi.getMetatraderAccountApi().getAccount("slaveMetaapiAccountId").get();
if (!slaveMetaapiAccount.getApplication().equals("CopyFactory")) {
  throw new Exception("Please specify CopyFactory application field value in your MetaApi account in order to use it in CopyFactory API");
}

// create CopyFactory master and slave accounts and connect them to MetaApi accounts via connectionId field
ConfigurationClient configurationApi = copyFactory.getConfigurationApi();
String masterAccountId = configurationApi.generateAccountId();
String slaveAccountId = configurationApi.generateAccountId();
configurationApi.updateAccount(masterAccountId, new CopyFactoryAccountUpdate() {{
  name = "Demo account";
  connectionId = masterMetaapiAccount.getId();
  subscriptions = List.of();
}}).get();

// create a strategy being copied
StrategyId strategyId = configurationApi.generateStrategyId().get();
configurationApi.updateStrategy(strategyId.id, new CopyFactoryStrategyUpdate() {{
  name = "Test strategy";
  description = "Some useful description about your strategy";
  positionLifecycle = "hedging";
  connectionId = masterMetaapiAccount.getId();
  maxTradeRisk = 0.1;
  stopOutRisk = new CopyFactoryStrategyStopOutRisk() {{
    value = 0.4;
    startTime = new IsoTime("2020-08-24T00:00:00.000Z");
  }},
  timeSettings = new CopyFactoryStrategyTimeSettings() {{
    lifetimeInHours = 192;
    openingIntervalInMinutes = 5;
  }}
}}).get();

// subscribe slave CopyFactory accounts to the strategy
configurationApi.updateAccount(slaveAccountId, new CopyFactoryAccountUpdate() {{
  name = "Demo account";
  connectionId = slaveMetaapiAccount.getId();
  subscriptions: List.of(new CopyFactoryStrategySubscription() {{
    strategyId = strategyId.id;
    multiplier = 1;
  }})
}}).get();
```

See jsdoc in-code documentation for full definition of possible configuration options.

### Retrieving trade copying history

CopyFactory allows you to monitor transactions conducted on trading accounts in real time.

#### Retrieving trading history on provider side
```java
HistoryClient historyApi = copyFactory.getHistoryApi();
// retrieve list of subscribers
System.out.println(historyApi.getSubscribers().get());
// retrieve list of strategies provided
System.out.println(historyApi.getProvidedStrategies().get());
// retrieve trading history, please note that this method support pagination and limits number of records
System.out.println(historyApi.getProvidedStrategiesTransactions(new IsoTime("2020-08-01T00:00:00.000Z"), new IsoTime("2020-09-01T00:00:00.000Z")).get());
```

#### Retrieving trading history on subscriber side
```java
HistoryApi historyApi = copyFactory.getHistoryApi();
// retrieve list of providers
System.out.println(historyApi.getProviders().get());
// retrieve list of strategies subscribed to
System.out.println(historyApi.getStrategiesSubscribed().get());
// retrieve trading history, please note that this method support pagination and limits number of records
System.out.println(historyApi.getStrategiesSubscribedTransactions(new IsoTime("2020-08-01T00:00:00.000Z"), new IsoTime("2020-09-01T00:00:00.000Z")).get());
```

#### Resynchronizing slave accounts to masters
There is a configurable time limit during which the trades can be opened. Sometimes trades can not open in time due to broker errors or trading session time discrepancy.
You can resynchronize a slave account to place such late trades. Please note that positions which were
closed manually on a slave account will also be reopened during resynchronization.

```java
String accountId = "..."; // CopyFactory account id
// resynchronize all strategies
copyFactory.getTradingApi().resynchronize(accountId).get();
// resynchronize specific strategy
copyFactory.tradingApi.resynchronize(accountId, List.of("ABCD")).get();
```

#### Managing stopouts
A subscription to a strategy can be stopped if the strategy have exceeded allowed risk limit.
```java
TradingClient tradingApi = copyFactory.getTradingApi();
String accountId = "..."; // CopyFactory account id
String strategyId = "..."; // CopyFactory strategy id

// retrieve list of strategy stopouts
System.out.println(tradingApi.getStopouts(accountId).get());

// reset a stopout so that subscription can continue
tradingApi.resetStopout(accountId, strategyId, "daily-equity").get();
```

#### Retrieving slave trading logs
```java
TradingClient tradingApi = copyFactory.getTradingApi();
String accountId = "..."; // CopyFactory account id
// retrieve slave trading log
System.out.println(tradingApi.getUserLog(accountId).get());
// retrieve paginated slave trading log by time range
IsoTime from = new IsoTime(Date.from(Instant.now().minusSeconds(24 * 60 * 60)));
System.out.println(tradingApi.getUserLog(accountId, from, null, 20, 10).get());
System.out.println(tradingApi.getUserLog(accountId, from, null, 20, 10).get());
```

## Related projects:
See our website for the full list of APIs and features supported [https://metaapi.cloud/#features](https://metaapi.cloud/#features)

Some of the APIs you might decide to use together with this module:

1. MetaApi cloud forex trading API [https://metaapi.cloud/docs/client/](https://metaapi.cloud/docs/client/)
2. MetaStats cloud forex trading statistics API [https://metaapi.cloud/docs/metastats/](https://metaapi.cloud/docs/metastats/)