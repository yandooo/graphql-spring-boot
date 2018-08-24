package com.graphql.spring.boot.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

public class GraphQLResponse {

    private ResponseEntity<String> responseEntity;
    private ObjectMapper mapper;
    private ReadContext context;

    public GraphQLResponse(ResponseEntity<String> responseEntity) {
        this.responseEntity = Objects.requireNonNull(responseEntity);
        this.mapper = new ObjectMapper();
        context = JsonPath.parse(responseEntity.getBody());
    }

    public JsonNode readTree() throws IOException {
        return mapper.readTree(responseEntity.getBody());
    }

    public String get(String path) {
        return context.read(path);
    }

    public ReadContext context() {
        return context;
    }

    public boolean isOk() {
        return getStatusCode() == HttpStatus.OK;
    }

    public HttpStatus getStatusCode() {
        return responseEntity.getStatusCode();
    }

    public ResponseEntity<String> getRawResponse() {
        return responseEntity;
    }
}
