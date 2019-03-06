# GraphQL and Graph*i*QL Spring Framework Boot Starters
[![Build Status](https://travis-ci.org/graphql-java-kickstart/graphql-spring-boot.svg?branch=master)](https://travis-ci.org/graphql-java-kickstart/graphql-spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/com.graphql-java-kickstart/graphql-spring-boot-starter.svg)](https://maven-badges.herokuapp.com/maven-central/com.graphql-java-kickstart/graphql-spring-boot-starter)
[![Chat on Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/graphql-java-kickstart/Lobby)

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [GraphQL and Graph*i*QL Spring Framework Boot Starters](#graphql-and-graphiql-spring-framework-boot-starters)
  - [WARNING: NoClassDefFoundError when using GraphQL Java Tools > 5.4.x](#warning-noclassdeffounderror-when-using-graphql-java-tools--54x)
    - [Using Gradle](#using-gradle)
    - [Using Maven](#using-maven)
- [Documentation](#documentation)
- [Requirements and Downloads](#requirements-and-downloads)
- [Enable GraphQL Servlet](#enable-graphql-servlet)
- [Enable Graph*i*QL](#enable-graphiql)
- [Supported GraphQL-Java Libraries](#supported-graphql-java-libraries)
  - [GraphQL Java Tools](#graphql-java-tools)
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
  compile 'com.graphql-java-kickstart:graphql-spring-boot-starter:5.7.0'
  
  // to embed Altair tool
  compile 'com.graphql-java-kickstart:altair-spring-boot-starter:5.7.0'

  // to embed GraphiQL tool
  compile 'com.graphql-java-kickstart:graphiql-spring-boot-starter:5.7.0'

  // to embed Voyager tool
  compile 'com.graphql-java-kickstart:voyager-spring-boot-starter:5.7.0'
  
  // testing facilities
  testCompile 'com.graphql-java-kickstart:graphql-spring-boot-starter-test:5.7.0'
}
```

Maven:
```xml
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphql-spring-boot-starter</artifactId>
    <version>5.7.0</version>
</dependency>

<!-- to embed Altair tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>altair-spring-boot-starter</artifactId>
    <version>5.7.0</version>
</dependency>

<!-- to embed GraphiQL tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphiql-spring-boot-starter</artifactId>
    <version>5.7.0</version>
</dependency>

<!-- to embed Voyager tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>voyager-spring-boot-starter</artifactId>
    <version>5.7.0</version>
</dependency>

<!-- testing facilities -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphql-spring-boot-starter-test</artifactId>
    <version>5.7.0</version>
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


# Contributions

Contributions are welcome.  Please respect the [Code of Conduct](http://contributor-covenant.org/version/1/3/0/).


# Licenses

`graphql-spring-boot-starter`, `altair-spring-boot-starter`, `graphiql-spring-boot-starter`and `voyager-spring-boot-starter` are licensed under the MIT License. See [LICENSE](LICENSE.md) for details.

[graphql-java License](https://github.com/andimarek/graphql-java/blob/master/LICENSE.md)

[graphiql License](https://github.com/graphql/graphiql/blob/master/LICENSE)

[graphql-js License](https://github.com/graphql/graphql-js/blob/master/LICENSE)
