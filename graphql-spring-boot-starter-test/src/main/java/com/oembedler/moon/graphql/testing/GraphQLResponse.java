package com.oembedler.moon.graphql.testing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

public class GraphQLResponse {

    private ResponseEntity<String> responseEntity;
    private ObjectMapper mapper;

    public GraphQLResponse(ResponseEntity<String> responseEntity) {
        this.responseEntity = Objects.requireNonNull(responseEntity);
        this.mapper = new ObjectMapper();
    }

    public JsonNode readTree() throws IOException {
        return mapper.readTree(responseEntity.getBody());
    }

}
