<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [GraphQL and GraphiQL Spring Framework Boot Starters](#graphql-and-graphiql-spring-framework-boot-starters)
- [Intro](#intro)
- [Requires](#requires)
- [Enable GraphQL Server](#enable-graphql-server)
- [Enable GraphiQL Tool](#enable-graphiql-tool)
- [Contributions](#contributions)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# GraphQL and GraphiQL Spring Framework Boot Starters
[![Build Status](https://travis-ci.org/graphql-java/graphql-spring-boot.svg?branch=master)](https://travis-ci.org/graphql-java/graphql-spring-boot)
[![Chat on Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/graphql-java/graphql-java)

GraphQL Starter 

[ ![Download](https://api.bintray.com/packages/oembedler/maven/graphql-spring-boot-starter/images/download.svg) ](https://bintray.com/oembedler/maven/graphql-spring-boot-starter/_latestVersion)

GraphiQL Starter 

[ ![Download](https://api.bintray.com/packages/oembedler/maven/graphiql-spring-boot-starter/images/download.svg) ](https://bintray.com/oembedler/maven/graphiql-spring-boot-starter/_latestVersion)

# Intro

Repository contains:

* `graphql-spring-boot-starter` to turn your boot application into GraphQL server (see [express-graphql](https://github.com/graphql/express-graphql))
* `graphiql-spring-boot-starter`to embed `GraphiQL` tool for schema introspection and query debugging (see [graphiql](https://github.com/graphql/graphiql))

# Requires

  * Java 1.8
  * [Spring Framework GraphQL Common Library](https://github.com/oembedler/spring-graphql-common)
  * Spring Framework Boot 1.4.x (web)

Add repository:

```gradle
repositories {
    // stable build
    jcenter()
    // development build
    maven { url  "http://dl.bintray.com/oembedler/maven" }
}
```

Dependency:

```gradle
dependencies {
  compile 'com.embedler.moon.graphql.boot:graphql-spring-boot-starter:INSERT_LATEST_VERSION_HERE'
  
  // to embed GraphiQL tool
  compile 'com.embedler.moon.graphql.boot:graphiql-spring-boot-starter:INSERT_LATEST_VERSION_HERE'
}
```

How to use the latest build with Maven:

```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>bintray-oembedler-maven</id>
    <name>bintray</name>
    <url>http://dl.bintray.com/oembedler/maven</url>
</repository>
```

Dependency:

```xml
<dependency>
    <groupId>com.embedler.moon.graphql.boot</groupId>
    <artifactId>graphql-spring-boot-starter</artifactId>
    <version>INSERT_LATEST_VERSION_HERE</version>
</dependency>

<!-- to embed GraphiQL tool -->
<dependency>
    <groupId>com.embedler.moon.graphql.boot</groupId>
    <artifactId>graphiql-spring-boot-starter</artifactId>
    <version>INSERT_LATEST_VERSION_HERE</version>
</dependency>
```


# Enable GraphQL Server

Server becomes accessible at `/graphql` if `graphql-spring-boot-starter` added as a dependency to a boot application
and `@EnableGraphQLServer` annotation is set at the main java configuration class.
GraphQL schemas are automatically discovered extracting all classes from Spring context marked as `@GraphQLSchema`.

Request parameters:

  * **`query`**: A string GraphQL document to be executed.

  * **`variables`**: The runtime values to use for any GraphQL query variables
    as a JSON object.

  * **`operationName`**: If the provided `query` contains multiple named
    operations, this specifies which operation should be executed. If not
    provided, an error will be returned if the `query` contains multiple
    named operations.

GraphQL will first look for each parameter in the URL's query-string:

```
/graphql?query=query+getUser($id:ID){user(id:$id){name}}&variables={"id":"4"}
```

If not found in the query-string, it will look in the POST request body.

Server uses [`commons-fileupload`][] middleware to add support
for `multipart/form-data` content, which may be useful for GraphQL mutations
involving uploading files (see test application for more details).
 `GraphQLContext` is a map of objects (context) for the current query execution.
In order to get access to uploaded file 
`com.oembedler.moon.graphql.boot.GraphQLContext` should be passed as input parameter to datafetcher or mutation (don't need to be annotated).
Calling method `GraphQLContext.getUploadedFile()` returns instance of [MultipartFile](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html).

If the POST body has not yet been parsed, graphql-express will interpret it
depending on the provided *Content-Type* header.

  * **`application/json`**: the POST body will be parsed as a JSON
    object of parameters.

  * **`application/x-www-form-urlencoded`**: this POST body will be
    parsed as a url-encoded string of key-value pairs.
    
  * **`multipart/form-data`**: this POST body will be
    parsed as a string of key-value pairs and it supports file upload (see above).    

  * **`application/graphql`**: The POST body will be parsed as GraphQL
    query string, which provides the `query` parameter.

Available Spring Boot configuration parameters (either `application.yml` or `application.properties`):

Server can host multiple schemas (all are registered at the startup time). 

To run query against particular schema - HTTP header `graphql-schema` parameter passed along with the query should contain graphql schema name of interest.

```yaml
graphql:
      server:
               mapping: /graphql
               corsEnabled: true
               suppressSpringResponseCodes: true
               query-key: query
               variables-key: variables
               uploadMaxFileSize: 128KB
               uploadMaxRequestSize: 128KB
      schema:
               clientMutationIdName: clientMutationId
               injectClientMutationId: true
               allowEmptyClientMutationId: false
               mutationInputArgumentName: input
               outputObjectNamePrefix: Payload
               inputObjectNamePrefix: Input
               schemaMutationObjectName: Mutation
```

To facilitate access from Nodejs frontend to GraphQL backend by default system enables global CORS filter for `/graphql/**` context.
The `corsEnabled` can be set to `false` to disable it.

By default system register `GlobalDefaultExceptionHandler` which suppresses Spring framework error responses and responds with standard GraphQL server error.
Application configuration `suppressSpringResponseCodes` property can be set to `false` to disable that handler.

# Enable GraphiQL Tool

Tool becomes accessible at the root `/` if `graphiql-spring-boot-starter` added as a dependency to a boot application.

Note that GraphQL server must be available at `/graphql` context to be discovered by GraphiQL.

# Contributions

Contributions are welcome.

Tips:

- Respect the [Code of Conduct](http://contributor-covenant.org/version/1/3/0/).
- Before opening an Issue to report a bug, please try the latest development version. 
It might happen that the problem is already solved.
- Please use  Markdown to format your comments properly. 
If you are not familiar with that: [Getting started with writing and formatting on GitHub](https://help.github.com/articles/getting-started-with-writing-and-formatting-on-github/)
- For Pull Requests:
  - Here are some [general tips](https://github.com/blog/1943-how-to-write-the-perfect-pull-request)
  - Please be a as focused and clear as possible and don't mix concerns. 
    This includes refactorings mixed with bug-fixes/features, see [Open Source Contribution Etiquette](http://tirania.org/blog/archive/2010/Dec-31.html) 
  - It would be good to add an automatic test(s). 
  

# License

`graphql-spring-boot-starter` and `graphiql-spring-boot-starter` are licensed under the MIT License. See [LICENSE](LICENSE.md) for details.

[spring-graphql-common License](https://github.com/oembedler/spring-graphql-common/blob/master/LICENSE.md)

[graphql-java License](https://github.com/andimarek/graphql-java/blob/master/LICENSE.md)

[graphiql License](https://github.com/graphql/graphiql/blob/master/LICENSE)

[graphql-js License](https://github.com/graphql/graphql-js/blob/master/LICENSE)
