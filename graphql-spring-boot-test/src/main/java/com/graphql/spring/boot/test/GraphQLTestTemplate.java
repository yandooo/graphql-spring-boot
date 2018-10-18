package com.graphql.spring.boot.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GraphQLTestTemplate {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired(required = false)
    private TestRestTemplate restTemplate;
    @Value("${graphql.servlet.mapping:/graphql}")
    private String graphqlMapping;

    private ObjectMapper objectMapper = new ObjectMapper();

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
     * @deprecated Use {@link #postForResource(String)} instead
     *
     * @param graphqlResource path to the classpath resource containing the GraphQL query
     * @return <tt>GraphQLResponse</tt> containing the result of query execution
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

    /**
     * Loads a GraphQL query from the given classpath resource and sends it to the GraphQL server.
     *
     * @param graphqlResource path to the classpath resource containing the GraphQL query
     * @return <tt>GraphQLResponse</tt> containing the result of query execution
     * @throws IOException if the resource cannot be loaded from the classpath
     */
    public GraphQLResponse postForResource(String graphqlResource) throws IOException {
        return perform(graphqlResource, null);
    }

    public GraphQLResponse postMultipart(String query, String variables) {
        return postRequest(RequestFactory.forMultipart(query, variables));
    }

    private GraphQLResponse post(String payload) {
        return postRequest(RequestFactory.forJson(payload));
    }

    private GraphQLResponse postRequest(HttpEntity<Object> request) {
        ResponseEntity<String> response = restTemplate.exchange(graphqlMapping, HttpMethod.POST, request, String.class);
        return new GraphQLResponse(response);
    }

}
