package com.graphql.spring.boot.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

class RequestFactory {

    private RequestFactory() {
        // utility class
    }

    static HttpEntity<Object> forJson(String json, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(json, headers);
    }

    static HttpEntity<Object> forMultipart(String query, String variables, HttpHeaders headers) {
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        LinkedMultiValueMap<String, Object> values = new LinkedMultiValueMap<>();
        values.add("query", forJson(query, headers));
        values.add("variables", forJson(variables, headers));
        return new HttpEntity<>(values, headers);
    }

}
