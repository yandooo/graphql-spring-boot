# GraphQL and Graph*i*QL Spring Framework Boot Starters
[![Build Status](https://travis-ci.org/graphql-java-kickstart/graphql-spring-boot.svg?branch=master)](https://travis-ci.org/graphql-java-kickstart/graphql-spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/com.graphql-java-kickstart/graphql-spring-boot-starter.svg)](https://maven-badges.herokuapp.com/maven-central/com.graphql-java-kickstart/graphql-spring-boot-starter)
[![Chat on Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/graphql-java-kickstart/Lobby)

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [Intro](#intro)
- [Requirements and Downloads](#requirements-and-downloads)
- [Enable GraphQL Servlet](#enable-graphql-servlet)
- [Enable GraphiQL](#enable-graphiql)
- [Supported GraphQL-Java Libraries](#supported-graphql-java-libraries)
    - [GraphQL Java Tools](#graphql-java-tools) - [https://github.com/graphql-java-kickstart/graphql-java-tools](https://github.com/graphql-java-kickstart/graphql-java-tools)
    - [GraphQL Spring Common](#graphql-spring-common) - [https://github.com/oembedler/spring-graphql-common](https://github.com/oembedler/spring-graphql-common)
- [Contributions](#contributions)
- [Licenses](#licenses)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# Intro

Repository contains:

* `graphql-spring-boot-starter` to turn your boot application into GraphQL server (see [graphql-java-servlet](https://github.com/graphql-java-kickstart/graphql-java-servlet))
* `graphiql-spring-boot-starter`to embed `GraphiQL` tool for schema introspection and query debugging (see [graphiql](https://github.com/graphql/graphiql))

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
  compile 'com.graphql-java-kickstart:graphql-spring-boot-starter:5.0.4'
  
  // to embed GraphiQL tool
  compile 'com.graphql-java-kickstart:graphiql-spring-boot-starter:5.0.4'

  // to embed Voyager tool
  compile 'com.graphql-java-kickstart:voyager-spring-boot-starter:5.0.4'
}
```

Maven:
```xml
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphql-spring-boot-starter</artifactId>
    <version>5.0.4</version>
</dependency>

<!-- to embed GraphiQL tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>graphiql-spring-boot-starter</artifactId>
    <version>5.0.4</version>
</dependency>

<!-- to embed Voyager tool -->
<dependency>
    <groupId>com.graphql-java-kickstart</groupId>
    <artifactId>voyager-spring-boot-starter</artifactId>
    <version>5.0.4</version>
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
    static:
      basePath: /
    enabled: true
    pageTitle: GraphiQL
    cdn:
        enabled: false
        version: 0.11.11
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
        schemaLocationPattern: "**/*.graphqls"
        # Enable or disable the introspection query. Disabling it puts your server in contravention of the GraphQL
        # specification and expectations of most clients, so use this option with caution
        introspectionEnabled: true
```
By default GraphQL tools uses the location pattern `**/*.graphqls` to scan for GraphQL schemas on the classpath.
Use the `schemaLocationPattern` property to customize this pattern.


## GraphQL Spring Common [LATEST SUPPORTED VERSION: 3.1.1]
**https://github.com/oembedler/spring-graphql-common**

See the [Readme](https://github.com/oembedler/spring-graphql-common#usage) and the [example](https://github.com/graphql-java-kickstart/graphql-spring-boot/tree/master/example-spring-common) for usage instructions.

#### Application Properties
```yaml
graphql:
      spring-graphql-common:
               clientMutationIdName: clientMutationId
               injectClientMutationId: true
               allowEmptyClientMutationId: false
               mutationInputArgumentName: input
               outputObjectNamePrefix: Payload
               inputObjectNamePrefix: Input
               schemaMutationObjectName: Mutation
```


# Contributions

Contributions are welcome.  Please respect the [Code of Conduct](http://contributor-covenant.org/version/1/3/0/).


# Licenses

`graphql-spring-boot-starter` and `graphiql-spring-boot-starter` are licensed under the MIT License. See [LICENSE](LICENSE.md) for details.

[spring-graphql-common License](https://github.com/oembedler/spring-graphql-common/blob/master/LICENSE.md)

[graphql-java License](https://github.com/andimarek/graphql-java/blob/master/LICENSE.md)

[graphiql License](https://github.com/graphql/graphiql/blob/master/LICENSE)

[graphql-js License](https://github.com/graphql/graphql-js/blob/master/LICENSE)
