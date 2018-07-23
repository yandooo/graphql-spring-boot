package com.oembedler.moon.graphql.testing;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GraphQLTestUtils {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private TestRestTemplate restTemplate;

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

    public GraphQLResponse perform(String graphqlResource) throws IOException {
        return perform(graphqlResource,null);
    }

    public GraphQLResponse perform(String graphqlResource, ObjectNode variables) throws IOException {
        String graphql = loadQuery(graphqlResource);
        String payload = createJsonQuery(graphql,variables);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange("/graphql", HttpMethod.POST, httpEntity, String.class);

        return new GraphQLResponse(response);
    }

}
