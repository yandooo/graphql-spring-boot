# GraphQL and Graph*i*QL Spring Framework Boot Starters
[![Build Status](https://travis-ci.org/graphql-java-kickstart/graphql-spring-boot.svg?branch=master)](https://travis-ci.org/graphql-java-kickstart/graphql-spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/com.graphql-java-kickstart/graphql-spring-boot-starter.svg)](https://maven-badges.herokuapp.com/maven-central/com.graphql-java-kickstart/graphql-spring-boot-starter)
[![Chat on Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/graphql-java-kickstart/Lobby)

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

  - [WARNING: NoClassDefFoundError when using GraphQL Java Tools > 5.4.x](#warning-noclassdeffounderror-when-using-graphql-java-tools--54x)
    - [Using Gradle](#using-gradle)
    - [Using Maven](#using-maven)
- [Documentation](#documentation)
- [Requirements and Downloads](#requirements-and-downloads)
- [Enable GraphQL Servlet](#enable-graphql-servlet)
- [Enable Graph*i*QL](#enable-graphiql)
- [Enable Altair](#enable-altair)
- [Enable GraphQL Playground](#enable-graphql-playground)
  - [Basic settings](#basic-settings)
  - [CDN](#cdn)
  - [Customizing GraphQL Playground](#customizing-graphql-playground)
  - [Tabs](#tabs)
- [Supported GraphQL-Java Libraries](#supported-graphql-java-libraries)
  - [GraphQL Java Tools](#graphql-java-tools)
- [Tracing and Metrics](#tracing-and-metrics)
  - [Usage](#usage)
- [Contributions](#contributions)
- [Licenses](#licenses)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## WARNING: NoClassDefFoundError when using GraphQL Java Tools > 5.4.x

If you're using `graphl-java-tools` you need to set the `kotlin.version` in your Spring Boot project explicitly to 
version 1.3.10, because Spring Boot Starter parent currently overrides it with a 1.2.* version of Kotlin. 
`graphql-java-tools` requires 1.3.* however because of its coroutine support. If you don't override this version
you will run into a `NoClassDefFoundError`.

Spring Boot team has indicated the Kotlin version will be upgraded to 1.3 in Spring Boot 2.2.

### Using Gradle
Set the Kotlin version in your `gradle.properties`
```
kotlin.version=1.3.10
```

### Using Maven
Set the Kotlin version in your `<properties>` section
```xml
<properties>
  <kotlin.version>1.3.10</kotlin.version>
</properties>
```

# Documentation

See our new [Documentation](https://www.graphql-java-kickstart.com/spring-boot/).

Repository contains:

* `graphql-spring-boot-starter` to turn your boot application into GraphQL server (see [graphql-java-servlet](https://github.com/graphql-java-kickstart/graphql-java-servlet))
* `altair-spring-boot-starter`to embed `Altair` tool for schema introspection and query debugging (see [altair](https://github.com/imolorhe/altair))
* `graphiql-spring-boot-starter`to embed `GraphiQL` tool for schema introspection and query debugging (see [graphiql](https://github.com/graphql/graphiql))
* `playground-spring-boot-starter`to embed `GraphQL Playground` tool for schema introspection and query debugging (see [GraphQL Playground](https://github.com/prisma/graphql-playground))
* `voyager-spring-boot-starter`to embed `Voyager` tool for visually explore GraphQL APIs as an interactive graph (see [voyger](https://github.com/APIs-guru/graphql-voyager))

# Requirements and Downloads

Requirements:
  * Java 1.8
  * Spring Framework Boot > 2.x.x (web)

Gradle:

```gradle
repositories {
    jcenter()
    mavenCentral()
}

dependencies {
  compile 'com.graphql-java-kickstart:graphql-spring-boot-starter:5.9.0'
  
  // to embed Altair tool
  runtime 'com.graphql-java-kickstart:altair-spring-boot-starter:5.9.0'

  // to embed GraphiQL tool
  runtime 'com.graphql-java-kickstart:graphiql-spring-boot-starter:5.9.0'

  // to embed Voyager tool
  runtime 'com.graphql-java-kickstart:voyager-spring-boot-starter:5.9.0'
  
  // testing facilities
  testCompile 'com.graphql-java-kickstart:graphql-spring-boot-starter-test:5.9.0'
}
```

Maven:
```xml
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphql-spring-boot-starter</artifactId>
    <version>5.9.0</version>
</dependency>

<!-- to embed Altair tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>altair-spring-boot-starter</artifactId>
    <version>5.9.0</version>
    <scope>runtime</scope>
</dependency>

<!-- to embed GraphiQL tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphiql-spring-boot-starter</artifactId>
    <version>5.9.0</version>
    <scope>runtime</scope>
</dependency>

<!-- to embed Voyager tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>voyager-spring-boot-starter</artifactId>
    <version>5.9.0</version>
    <scope>runtime</scope>
</dependency>

<!-- testing facilities -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphql-spring-boot-starter-test</artifactId>
    <version>5.9.0</version>
    <scope>test</scope>
</dependency>

```

New releases will be available faster in the JCenter repository than in Maven Central. Add the following to use for Maven
```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```
For Gradle:
```groovy
repositories {
    jcenter()
}
```

# Enable GraphQL Servlet

The servlet becomes accessible at `/graphql` if `graphql-spring-boot-starter` added as a dependency to a boot application and a `GraphQLSchema` bean is present in the application.  Check out the [simple example](https://github.com/graphql-java-kickstart/graphql-spring-boot/tree/master/example) for the bare minimum required.

A GraphQL schema can also be automatically created when a [supported graphql-java schema library](https://github.com/graphql-java-kickstart/graphql-spring-boot/blob/master/README.md#supported-graphql-java-libraries) is found on the classpath.

See the [graphql-java-servlet usage docs](https://github.com/graphql-java-kickstart/graphql-java-servlet#usage) for the avaiable endpoints exposed.

Available Spring Boot configuration parameters (either `application.yml` or `application.properties`):

```yaml
graphql:
      servlet:
           mapping: /graphql
           enabled: true
           corsEnabled: true
           # if you want to @ExceptionHandler annotation for custom GraphQLErrors
           exception-handlers-enabled: true
```

By default a global CORS filter is enabled for `/graphql/**` context.
The `corsEnabled` can be set to `false` to disable it.

# Enable Graph*i*QL

Graph*i*QL becomes accessible at the root `/graphiql` if `graphiql-spring-boot-starter` is added as a dependency to a boot application.

Note that GraphQL server must be available at `/graphql/*` context to be discovered by Graph*i*QL.

Available Spring Boot configuration parameters (either `application.yml` or `application.properties`):
```yaml
graphiql:
    mapping: /graphiql
    endpoint:
      graphql: /graphql
      subscriptions: /subscriptions
    subscriptions:
      timeout: 30
      reconnect: false
    static:
      basePath: /
    enabled: true
    pageTitle: GraphiQL
    cdn:
        enabled: false
        version: 0.13.0
    props:
        resources:
            query: query.graphql
            defaultQuery: defaultQuery.graphql
            variables: variables.graphql
        variables:
            editorTheme: "solarized light"
    headers:
        Authorization: "Bearer <your-token>"
```
By default GraphiQL is served from within the package. This can be configured to be served from CDN instead,
by setting the property `graphiql.cdn.enabled` to `true`.

You are able to set the GraphiQL props as well. The `graphiql.props.variables` group can contain any of the props
as defined at [GraphiQL Usage](https://github.com/graphql/graphiql#usage). Since setting (large) queries in the
properties like this isn't very readable, you can use the properties in the `graphiql.props.resources` group
to set the classpath resources that should be loaded.

Headers that are used when sending the GraphiQL queries can be set by defining them in the `graphiql.headers` group.

# Enable Altair

Altair becomes accessible at the root `/altair` if `altair-spring-boot-starter` is added as a dependency to a boot application.

Note that GraphQL server must be available at `/graphql/*` context to be discovered by Altair.

Available Spring Boot configuration parameters (either `application.yml` or `application.properties`):
```yaml
altair:
    mapping: /altair
    endpoint:
      graphql: /graphql
      subscriptions: /subscriptions
    subscriptions:
      timeout: 30
      reconnect: false
    static:
      basePath: /
    enabled: true
    pageTitle: Altair
    cdn:
        enabled: false
        version: 2.1.7
    props:
        resources:
            defaultQuery: defaultQuery.graphql
            variables: variables.graphql
    headers:
        Authorization: "Bearer <your-token>"
```
By default Altair is served from within the package. This can be configured to be served from CDN instead,
by setting the property `altair.cdn.enabled` to `true`.

You are able to set the Altair props as well. The `altair.props.variables` group. Since setting (large) queries in the
properties like this isn't very readable, you can use the properties in the `altair.props.resources` group
to set the classpath resources that should be loaded.

Headers that are used when sending the Altair queries can be set by defining them in the `altair.headers` group.

# Enable GraphQL Playground

GraphQL Playground becomes accessible at root `/playground` (or as configured in `graphql.playground.mapping`) 
if `playground-spring-boot-starter` is added as a dependency to a boot application. 

It uses an embedded `GraphQL Playground React`, in accordance to the [official guide](https://github.com/prisma/graphql-playground#as-html-page),
using the 'minimum HTML' approach.

Available Spring Boot configuration parameters (either `application.yml` or `application.properties`):

```yaml
graphql.playground:
    mapping: /playground
    endpoint: /graphql
    subscriptionsEndpoint: /subscriptions
    enabled: true
    pageTitle: Playground
    cdn:
        enabled: false
        version: latest
    settings:
        editor.cursorShape: line
        editor.fontFamily: "'Source Code Pro', 'Consolas', 'Inconsolata', 'Droid Sans Mono', 'Monaco', monospace"
        editor.fontSize: 14
        editor.reuseHeaders: true
        editor.theme: dark
        general.betaUpdates: false
        prettier.printWidth: 80
        prettier.tabWidth: 2
        prettier.useTabs: false
        request.credentials: omit
        schema.polling.enable: true
        schema.polling.endpointFilter: "*localhost*"
        schema.polling.interval: 2000
        schema.disableComments: true
        tracing.hideTracingResponse: true
    headers:
        headerFor: AllTabs
    tabs:
        - name: Example Tab
          query: classpath:exampleQuery.graphql
          headers:
            SomeHeader: Some value
          variables: classpath:variables.json
          responses:
            - classpath:exampleResponse1.json
            - classpath:exampleResponse2.json
```
## Basic settings

`mapping`, `endpoint` and `subscriptionsEndpoint` will default to `/playground`, `/graphql` and `/subscriptions`, 
respectively. Note that these values may not be empty.

`enabled` defaults to `true`, and therefor Playground will be available by default if the dependency is added to a
 Spring Boot Web Application project.

`pageTitle` defaults to `Playground`.

`headers` allows you to specify headers for the default tab. Note that if your are using Spring Security and CSRF is 
enabled CSRF, the CSRF token will be automatically added to the headers. These headers will also be added to all the tabs
configured under the [Tabs](#tabs) section. If a header is defined both in this 'global' header list and the header list
of the individual tabs, the 'local' version will be used for that tab. 

## CDN

The currently bundled version is `1.7.20`, which is - as of writing this - the latest release of `GraphQL Playground React`. 
The CDN option uses `jsDelivr` CDN, if enabled. By default, it will load the latest available release.
Available CDN versions can be found on the project's 
[jsDelivr page](https://www.jsdelivr.com/package/npm/graphql-playground-react). The CDN option is disabled by default.

## Customizing GraphQL Playground

Further GraphQL Playground settings can be specified under the `settings` group, which are documented in the official 
[GraphQL Playground readme](https://github.com/prisma/graphql-playground#settings). Note that enum-like values are 
validated against the available options, and your application will not start if wrong settings are provided. Similarly
there is some basic validation for integer values (they must be valid positive integers).

## Tabs

Optionally, you can specify tabs that will be present when the user first opens GraphQL Playground. You can configure the 
query, variables, headers and even supply sample responses. Note that `query`, `variables` and `responses` are expected
to be resources of the appropriate format (GraphQL for `query`, JSON for `variables` and `responses`). 

# Supported GraphQL-Java Libraries

The following libraries have auto-configuration classes for creating a `GraphQLSchema`.

## GraphQL Java Tools
**https://github.com/graphql-java-kickstart/graphql-java-tools**

All `GraphQLResolver` and `GraphQLScalar` beans, along with a bean of type `SchemaParserDictionary` (to provide all other classes), will be used to create a GraphQLSchema.  Any files on the classpath named `*.graphqls` will be used to provide the schema definition.  See the [Readme](https://github.com/graphql-java-kickstart/graphql-java-tools#usage) for more info.

Available Spring Boot configuration parameters (either `application.yml` or `application.properties`):

```yaml
graphql:
    tools:
        schema-location-pattern: "**/*.graphqls"
        # Enable or disable the introspection query. Disabling it puts your server in contravention of the GraphQL
        # specification and expectations of most clients, so use this option with caution
        introspection-enabled: true
```
By default GraphQL tools uses the location pattern `**/*.graphqls` to scan for GraphQL schemas on the classpath.
Use the `schemaLocationPattern` property to customize this pattern.

# Tracing and Metrics

[Apollo style tracing](https://github.com/apollographql/apollo-tracing) along with two levels of metrics based on them are currently configurable.
Full tracing is based on the GraphQl java implementation, and can be enabled in the application.yml or application.properties file:
```yaml
graphql:
    servlet:
        tracing-enabled: true
```
the default value is false, with "metrics-only" being available. Metrics-only does not add the tracing extension to the response.

Metrics utilize one of two forms of tracing to feed information to Micrometer. If tracing is enabled, or set to "metrics-only", full tracing metrics will be collected, otherwise a tracing implementation that does not collect field data will be injected. Metrics can be configured in the application.yml or application.properties to either true or false, with a default of false:
```yaml
graphql:
    servlet:
        actuator-metrics: true
```

## Usage
See [Baeldung Spring Boot Actuators](https://www.baeldung.com/spring-boot-actuators) for the basics of using Actuator. 
Add `spring-boot-starter-actuator` to your project as dependency.

The following metrics are available for exposure:
* `graphql.timer.query`
* `graphql.websocket.sessions` - number of active websocket sessions for subscriptions
* `graphql.websocket.subscriptions` - number of active subscriptions 


# Contributions

Contributions are welcome.  Please respect the [Code of Conduct](http://contributor-covenant.org/version/1/3/0/).


# Licenses

`graphql-spring-boot-starter`, `altair-spring-boot-starter`, `graphiql-spring-boot-starter`and `voyager-spring-boot-starter` are licensed under the MIT License. See [LICENSE](LICENSE.md) for details.

[graphql-java License](https://github.com/andimarek/graphql-java/blob/master/LICENSE.md)

[graphiql License](https://github.com/graphql/graphiql/blob/master/LICENSE)

[graphql-js License](https://github.com/graphql/graphql-js/blob/master/LICENSE)
