/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 oEmbedler Inc. and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.oembedler.moon.graphql.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.oembedler.moon.graphql.engine.GraphQLSchemaHolder;
import com.oembedler.moon.graphql.engine.execute.GraphQLQueryExecutor;
import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@RestController
@RequestMapping("${spring.graphql.server.mapping:/graphql}")
public class GraphQLServerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLServerController.class);

    public static final String DEFAULT_QUERY_KEY = "query";
    public static final String DEFAULT_VARIABLES_KEY = "variables";
    public static final String DEFAULT_OPERATION_NAME_KEY = "operationName";
    public static final String DEFAULT_DATA_KEY = "data";
    public static final String DEFAULT_FILENAME_UPLOAD_KEY = "file";
    public static final String DEFAULT_ERRORS_KEY = "errors";
    public static final String HEADER_SCHEMA_NAME = "graphql-schema";

    // ---

    @Autowired
    private GraphQLProperties graphQLProperties;
    @Autowired
    private GraphQLSchemaLocator graphQLSchemaLocator;

    private ObjectMapper objectMapper = new ObjectMapper();

    // ---

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getJson(@RequestParam(DEFAULT_QUERY_KEY) String query,
                                                       @RequestParam(value = DEFAULT_VARIABLES_KEY, required = false) String variables,
                                                       @RequestParam(value = DEFAULT_OPERATION_NAME_KEY, required = false) String operationName,
                                                       @RequestHeader(value = HEADER_SCHEMA_NAME, required = false) String graphQLSchemaName,
                                                       HttpServletRequest httpServletRequest) throws IOException {

        final GraphQLContext graphQLContext = new GraphQLContext();
        graphQLContext.setHttpRequest(httpServletRequest);

        final Map<String, Object> result = evaluateAndBuildResponseMap(query, operationName, graphQLContext, decodeIntoMap(variables), graphQLSchemaName);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/graphql")
    public ResponseEntity<Map<String, Object>> postGraphQL(@RequestBody String query,
                                                           @RequestParam(value = DEFAULT_OPERATION_NAME_KEY, required = false) String operationName,
                                                           @RequestHeader(value = HEADER_SCHEMA_NAME, required = false) String graphQLSchemaName,
                                                           HttpServletRequest httpServletRequest) {

        final GraphQLContext graphQLContext = new GraphQLContext();
        graphQLContext.setHttpRequest(httpServletRequest);

        final Map<String, Object> result = evaluateAndBuildResponseMap(query, operationName, graphQLContext, new HashMap<>(), graphQLSchemaName);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Map<String, Object>> postJson(@RequestBody Map<String, Object> body,
                                                        @RequestHeader(value = HEADER_SCHEMA_NAME, required = false) String graphQLSchemaName,
                                                        HttpServletRequest httpServletRequest) {

        final String query = (String) body.get(getQueryKey());
        final String operationName = (String) body.get(DEFAULT_OPERATION_NAME_KEY);

        Map<String, Object> variables = null;
        Object variablesObject = body.get(getVariablesKey());
        if (variablesObject != null && variablesObject instanceof Map)
            variables = (Map<String, Object>) variablesObject;

        final GraphQLContext graphQLContext = new GraphQLContext();
        graphQLContext.setHttpRequest(httpServletRequest);

        final Map<String, Object> result = evaluateAndBuildResponseMap(query, operationName, graphQLContext, variables, graphQLSchemaName);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam(DEFAULT_FILENAME_UPLOAD_KEY) MultipartFile file,
                                                          @RequestParam(DEFAULT_QUERY_KEY) String query,
                                                          @RequestParam(value = DEFAULT_VARIABLES_KEY, required = false) String variables,
                                                          @RequestParam(value = DEFAULT_OPERATION_NAME_KEY, required = false) String operationName,
                                                          @RequestHeader(value = HEADER_SCHEMA_NAME, required = false) String graphQLSchemaName,
                                                          HttpServletRequest httpServletRequest) throws IOException {

        final GraphQLContext graphQLContext = new GraphQLContext();
        graphQLContext.setUploadedFile(file);
        graphQLContext.setHttpRequest(httpServletRequest);

        final Map<String, Object> result = evaluateAndBuildResponseMap(query, operationName, graphQLContext, decodeIntoMap(variables), graphQLSchemaName);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Map<String, Object>> uploadSmallFile(@RequestParam(DEFAULT_QUERY_KEY) String query,
                                                               @RequestParam(value = DEFAULT_VARIABLES_KEY, required = false) String variables,
                                                               @RequestParam(value = DEFAULT_OPERATION_NAME_KEY, required = false) String operationName,
                                                               @RequestHeader(value = HEADER_SCHEMA_NAME, required = false) String graphQLSchemaName,
                                                               HttpServletRequest httpServletRequest) throws IOException {

        final GraphQLContext graphQLContext = new GraphQLContext();
        graphQLContext.setHttpRequest(httpServletRequest);

        final Map<String, Object> result = evaluateAndBuildResponseMap(query, operationName, graphQLContext, decodeIntoMap(variables), graphQLSchemaName);
        return ResponseEntity.ok(result);
    }

    private Map<String, Object> decodeIntoMap(final String variablesParam) throws IOException {
        return objectMapper.readValue(variablesParam, Map.class);
    }

    private Map<String, Object> evaluateAndBuildResponseMap(final String query,
                                                            final String operationName,
                                                            final GraphQLContext graphQLContext,
                                                            final Map<String, Object> variables,
                                                            final String graphQLSchemaName) {
        final Map<String, Object> result = new LinkedHashMap<>();
        final GraphQLSchemaHolder graphQLSchemaHolder = getGraphQLSchemaContainer(graphQLSchemaName);
        final ExecutionResult executionResult = evaluate(query, operationName, graphQLContext, variables, graphQLSchemaHolder);

        if (executionResult.getErrors().size() > 0) {
            result.put(DEFAULT_ERRORS_KEY, executionResult.getErrors());
            LOGGER.error("Errors: {}", executionResult.getErrors());
        }

        result.put(DEFAULT_DATA_KEY, executionResult.getData());
        return result;
    }

    private ExecutionResult evaluate(final String query,
                                     final String operationName,
                                     final GraphQLContext graphQLContext,
                                     final Map<String, Object> variables,
                                     final GraphQLSchemaHolder graphQLSchemaHolder) {
        ExecutionResult executionResult;

        if (graphQLSchemaHolder == null) {
            executionResult = new ExecutionResultImpl(Lists.newArrayList(new ErrorGraphQLSchemaUndefined()));
        } else {
            try {
                GraphQLQueryExecutor graphQLQueryExecutor = GraphQLQueryExecutor.create(graphQLSchemaHolder)
                        .query(query).context(graphQLContext);

                if (variables != null)
                    graphQLQueryExecutor.arguments(variables);

                if (StringUtils.hasText(operationName))
                    graphQLQueryExecutor.operation(operationName);

                executionResult = graphQLQueryExecutor.execute();

            } catch (Exception e) {
                LOGGER.error("Error occurred evaluating query: {}", query);
                executionResult = new ExecutionResultImpl(Lists.newArrayList(new ErrorGraphQLQueryEvaluation()));
            }
        }

        return executionResult;
    }

    private String getQueryKey() {
        return StringUtils.hasText(graphQLProperties.getServer().getQueryKey()) ?
                graphQLProperties.getServer().getQueryKey() : DEFAULT_QUERY_KEY;
    }

    private String getVariablesKey() {
        return StringUtils.hasText(graphQLProperties.getServer().getVariablesKey()) ?
                graphQLProperties.getServer().getVariablesKey() : DEFAULT_VARIABLES_KEY;
    }

    public GraphQLSchemaHolder getGraphQLSchemaContainer(String graphQLSchema) {
        if (StringUtils.hasText(graphQLSchema))
            return graphQLSchemaLocator.getGraphQLSchemaHolder(graphQLSchema);
        else if (graphQLSchemaLocator.getTotalNumberOfSchemas() == 1)
            return graphQLSchemaLocator.getSingleSchema();

        return null;
    }
}
