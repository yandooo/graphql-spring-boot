package com.graphql.spring.boot.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GraphQLTestTemplate {

    private final ResourceLoader resourceLoader;
    private final TestRestTemplate restTemplate;
    private final String graphqlMapping;
    private final ObjectMapper objectMapper;
    @Getter
    private final HttpHeaders headers = new HttpHeaders();

    public GraphQLTestTemplate(
        final ResourceLoader resourceLoader,
        final TestRestTemplate restTemplate,
        @Value("${graphql.servlet.mapping:/graphql}")
        final String graphqlMapping,
        final ObjectMapper objectMapper
    ) {
        this.resourceLoader = resourceLoader;
        this.restTemplate = restTemplate;
        this.graphqlMapping = graphqlMapping;
        this.objectMapper = objectMapper;
    }

    private String createJsonQuery(String graphql, ObjectNode variables)
            throws JsonProcessingException {

        ObjectNode wrapper = objectMapper.createObjectNode();
        wrapper.put("query", graphql);
        wrapper.set("variables", variables);
        return objectMapper.writeValueAsString(wrapper);
    }

    private String loadQuery(String location) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + location);
        return loadResource(resource);
    }

    private String loadResource(Resource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }

    /**
     * @deprecated use {{@link #withAdditionalHeader(String, String...)}} instead
     * Add an HTTP header that will be sent with each request this sends.
     *
     * @param name Name (key) of HTTP header to add.
     * @param value Value of HTTP header to add.
     */
    @Deprecated
    public void addHeader(final String name, final String value) {
        withAdditionalHeader(name, value);
    }

    /**
     * Add an HTTP header that will be sent with each request this sends.
     *
     * @param name Name (key) of HTTP header to add.
     * @param value Value(s) of HTTP header to add.
     */
    public GraphQLTestTemplate withAdditionalHeader(final String name, final String... value) {
        headers.addAll(name, Arrays.asList(value));
        return this;
    }

    /**
     * Add multiple HTTP header that will be sent with each request this sends.
     *
     * @param additionalHeaders additional headers to add
     */
    public GraphQLTestTemplate withAdditionalHeaders(final MultiValueMap<String, String> additionalHeaders) {
        headers.addAll(additionalHeaders);
        return this;
    }

    /**
     * Adds a bearer token to the authorization header.
     * @param token the bearer token
     * @return self
     */
    public GraphQLTestTemplate withBearerAuth(@NonNull final String token) {
        headers.setBearerAuth(token);
        return this;
    }

    /**
     * Adds basic authentication to the authorization header.
     * @param username the username
     * @param password the password
     * @param charset the charset used by the credentials
     * @return self
     */
    public GraphQLTestTemplate withBasicAuth(
        @NonNull final String username,
        @NonNull final String password,
        @Nullable final Charset charset
    ) {
        headers.setBasicAuth(username, password, charset);
        return this;
    }

    /**
     * Adds basic authentication to the authorization header.
     * @param username the username
     * @param password the password
     * @return self
     */
    public GraphQLTestTemplate withBasicAuth(@NonNull final String username, @NonNull final String password) {
        headers.setBasicAuth(username, password, null);
        return this;
    }

    /**
     * Adds basic authentication to the authorization header.
     * @param encodedCredentials the encoded credentials
     * @return self
     */
    public GraphQLTestTemplate withBasicAuth(@NonNull final String encodedCredentials) {
        headers.setBasicAuth(encodedCredentials);
        return this;
    }

    /**
     * @deprecated use {{@link #withHeaders(HttpHeaders)}} instead.
     * Replace any associated HTTP headers with the provided headers.
     *
     * @param newHeaders Headers to use.
     */
    @Deprecated
    public void setHeaders(HttpHeaders newHeaders) {
        withHeaders(newHeaders);
    }

    /**
     * Replace any associated HTTP headers with the provided headers.
     *
     * @param newHeaders Headers to use.
     */
    public GraphQLTestTemplate withHeaders(final HttpHeaders newHeaders) {
       return withClearHeaders().withAdditionalHeaders(newHeaders);
    }

    /**
     * @deprecated use {{@link #withClearHeaders()}} instead
     * Clear all associated HTTP headers.
     */
    @Deprecated
    public void clearHeaders() {
        withClearHeaders();
    }

    /**
     * Clear all associated HTTP headers.
     * @return self
     */
    public GraphQLTestTemplate withClearHeaders() {
        headers.clear();
        return this;
    }

    /**
     * @deprecated Use {@link #postForResource(String)} instead
     *
     * @param graphqlResource path to the classpath resource containing the GraphQL query
     * @return GraphQLResponse containing the result of query execution
     * @throws IOException if the resource cannot be loaded from the classpath
     */
    public GraphQLResponse perform(String graphqlResource) throws IOException {
        return postForResource(graphqlResource);
    }

    public GraphQLResponse perform(String graphqlResource, ObjectNode variables) throws IOException {
        String graphql = loadQuery(graphqlResource);
        String payload = createJsonQuery(graphql, variables);
        return post(payload);
    }

    public GraphQLResponse perform(String graphqlResource, ObjectNode variables, List<String> fragmentResources) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String fragmentResource : fragmentResources) {
            sb.append(loadQuery(fragmentResource));
        }
        String graphql = sb.append(loadQuery(graphqlResource)).toString();
        String payload = createJsonQuery(graphql, variables);
        return post(payload);
    }

    /**
     * Loads a GraphQL query from the given classpath resource and sends it to the GraphQL server.
     *
     * @param graphqlResource path to the classpath resource containing the GraphQL query
     * @return GraphQLResponse containing the result of query execution
     * @throws IOException if the resource cannot be loaded from the classpath
     */
    public GraphQLResponse postForResource(String graphqlResource) throws IOException {
        return perform(graphqlResource, null);
    }

    /**
     * Loads a GraphQL query from the given classpath resource, appending any graphql fragment
     * resources provided  and sends it to the GraphQL server.
     *
     * @param graphqlResource path to the classpath resource containing the GraphQL query
     * @param fragmentResources an ordered list of classpaths containing GraphQL fragment definitions.
     * @return GraphQLResponse containing the result of query execution
     * @throws IOException if the resource cannot be loaded from the classpath
     */
    public GraphQLResponse postForResource(String graphqlResource, List<String> fragmentResources) throws IOException {
        return perform(graphqlResource, null, fragmentResources);
    }

    public GraphQLResponse postMultipart(String query, String variables) {
        return postRequest(RequestFactory.forMultipart(query, variables, headers));
    }

    private GraphQLResponse post(String payload) {
        return postRequest(RequestFactory.forJson(payload, headers));
    }

    private GraphQLResponse postRequest(HttpEntity<Object> request) {
        ResponseEntity<String> response = restTemplate.exchange(graphqlMapping, HttpMethod.POST, request, String.class);
        return new GraphQLResponse(response, objectMapper);
    }

}