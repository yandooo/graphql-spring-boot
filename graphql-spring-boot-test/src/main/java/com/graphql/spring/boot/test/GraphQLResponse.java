package com.graphql.spring.boot.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GraphQLResponse {

    private final ResponseEntity<String> responseEntity;
    private final ObjectMapper mapper;
    private final ReadContext context;

    public GraphQLResponse(ResponseEntity<String> responseEntity, ObjectMapper objectMapper) {
        this.responseEntity = Objects.requireNonNull(responseEntity);
        this.mapper = Objects.requireNonNull(objectMapper);

        Objects.requireNonNull(responseEntity.getBody(),
                () -> "Body is empty with status " + responseEntity.getStatusCodeValue());
        context = JsonPath.parse(responseEntity.getBody());
    }

    public JsonNode readTree() throws IOException {
        return mapper.readTree(responseEntity.getBody());
    }

    public String get(String path) {
        return get(path, String.class);
    }

    public <T> T get(String path, Class<T> type) {
        return mapper.convertValue(context.read(path, Object.class), type);
    }

    public <T> List<T> getList(String path, Class<T> type) {
        final List<?> raw = context.read(path, List.class);
        return mapper.convertValue(raw, mapper.getTypeFactory().constructCollectionType(List.class, type));
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
