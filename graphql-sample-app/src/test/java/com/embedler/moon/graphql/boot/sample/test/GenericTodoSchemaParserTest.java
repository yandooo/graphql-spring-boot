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

package com.embedler.moon.graphql.boot.sample.test;

import com.embedler.moon.graphql.boot.sample.ApplicationBootConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oembedler.moon.graphql.boot.GraphQLServerRequest;
import com.oembedler.moon.graphql.boot.GraphQLServerResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationBootConfiguration.class)
@WebIntegrationTest("server.port=0")
public class GenericTodoSchemaParserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericTodoSchemaParserTest.class);

    @Value("${local.server.port}")
    private int port;

    private RestTemplate restTemplate = new TestRestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void restViewerByIdTest() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        GraphQLServerRequest qlQuery = new GraphQLServerRequest("{viewer{ id }}");

        HttpEntity<GraphQLServerRequest> httpEntity = new HttpEntity<>(qlQuery, headers);
        ResponseEntity<GraphQLServerResult> responseEntity = restTemplate.exchange("http://localhost:" + port + "/graphql", HttpMethod.POST, httpEntity, GraphQLServerResult.class);

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertTrue(CollectionUtils.isEmpty(result.getErrors()));
        Assert.assertFalse(CollectionUtils.isEmpty(result.getData()));
        LOGGER.info(objectMapper.writeValueAsString(result.getData()));
    }

    @Test
    public void restSchemaDoesNotExistsTest() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("graphql-schema", "no-such-schema");

        GraphQLServerRequest qlQuery = new GraphQLServerRequest("{viewer{ id }}");

        HttpEntity<GraphQLServerRequest> httpEntity = new HttpEntity<>(qlQuery, headers);
        ResponseEntity<GraphQLServerResult> responseEntity = restTemplate.exchange("http://localhost:" + port + "/graphql", HttpMethod.POST, httpEntity, GraphQLServerResult.class);

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertFalse(CollectionUtils.isEmpty(result.getErrors()));
        LOGGER.info(objectMapper.writeValueAsString(result));
    }

    @Test
    public void restUnsupportedOperationErrorTest() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        GraphQLServerRequest qlQuery = new GraphQLServerRequest("{viewer{ id }}");
        HttpEntity<GraphQLServerRequest> httpEntity = new HttpEntity<>(qlQuery, headers);

        ResponseEntity<GraphQLServerResult> responseEntity = restTemplate.exchange("http://localhost:" + port + "/graphql", HttpMethod.PUT, httpEntity, GraphQLServerResult.class);

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertFalse(CollectionUtils.isEmpty(result.getErrors()));
        LOGGER.info(objectMapper.writeValueAsString(result));
    }

    @Test
    public void todoMutationPostJson() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("query", "mutation AddTodoMutationMutation($input: addTodoMutationInput!) {addTodoMutation(input: $input) {clientMutationId todoEdge {node {text}}}}");
        map.put("variables", "{\"input\": {\"clientMutationId\": \"3-3\",\"addTodoInput\": {\"text\": \"hi\"}}}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<GraphQLServerResult> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/graphql", HttpMethod.POST, requestEntity, GraphQLServerResult.class);

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertTrue(CollectionUtils.isEmpty(result.getErrors()));
        Assert.assertFalse(CollectionUtils.isEmpty(result.getData()));
        LOGGER.info(objectMapper.writeValueAsString(result.getData()));
    }

    @Test
    public void queryWithEmptyVariables() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("query", "{viewer{ id }}");
        map.put("variables", "");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<GraphQLServerResult> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/graphql", HttpMethod.POST, requestEntity, GraphQLServerResult.class);

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertTrue(CollectionUtils.isEmpty(result.getErrors()));
        Assert.assertFalse(CollectionUtils.isEmpty(result.getData()));
        LOGGER.info(objectMapper.writeValueAsString(result.getData()));
    }

    @Test
    public void restViewerJsonTest() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        String qlQuery = "{viewer{ id }}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("test-key", "key-0.9.0");
        ResponseEntity<GraphQLServerResult> responseEntity =
                restTemplate.getForEntity("http://localhost:" + port + "/graphql?query={0}&variables={1}",
                        GraphQLServerResult.class, qlQuery, objectMapper.writeValueAsString(variables));

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertTrue(CollectionUtils.isEmpty(result.getErrors()));
        Assert.assertFalse(CollectionUtils.isEmpty(result.getData()));
        LOGGER.info(objectMapper.writeValueAsString(result.getData()));
    }

    @Test
    public void restUploadFileTest() throws IOException {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new ClassPathResource("application.yml"));
        String qlQuery = "mutation UploadFileMutation{uploadFile(input: {clientMutationId:\"m-123\"}){ filename }}";
        map.add("query", qlQuery);
        map.add("variables", "{}");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<GraphQLServerResult> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/graphql", HttpMethod.POST, requestEntity, GraphQLServerResult.class);

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertTrue(CollectionUtils.isEmpty(result.getErrors()));
        Assert.assertFalse(CollectionUtils.isEmpty(result.getData()));
        LOGGER.info(objectMapper.writeValueAsString(result.getData()));
    }

    @Test
    public void restUploadFileUrlEncodedTest() throws IOException {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("query", "mutation AddTodoMutationMutation{addTodoMutation(input: {clientMutationId:\"m-123\", addTodoInput:{text: \"text\"}}){ clientMutationId, todoEdge {cursor, node {text, complete}} }}");
        map.add("variables", "{}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<GraphQLServerResult> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/graphql", HttpMethod.POST, requestEntity, GraphQLServerResult.class);

        GraphQLServerResult result = responseEntity.getBody();
        Assert.assertTrue(CollectionUtils.isEmpty(result.getErrors()));
        Assert.assertFalse(CollectionUtils.isEmpty(result.getData()));
        LOGGER.info(objectMapper.writeValueAsString(result.getData()));
    }

}
