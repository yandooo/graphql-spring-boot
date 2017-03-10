package com.oembedler.moon.graphql.boot;

import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Created by ladanyi on 3/10/17.
 * Interface implementation should be used to tweak result after request procecessing.
 * Primary goals were to anable custom exception and http result management.
 */
public interface CustomResultHandler {
    /**
     * @see GraphQLServerController
     *
     * @param result contains plain results from previous processing
     * @return method should return modified result wrapped in ResponseEntity.
     */
    ResponseEntity<Map<String,Object>> handle(Map<String, Object> result);
}
