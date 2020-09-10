package com.graphql.spring.boot.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphql.spring.boot.test.assertions.GraphQLErrorListAssertion;
import com.graphql.spring.boot.test.assertions.GraphQLFieldAssert;
import com.graphql.spring.boot.test.assertions.NumberOfErrorsAssertion;
import graphql.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContentAssert;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphQLResponseTest {

    private static final String DATA_PATH = "$.data.test";

    @Autowired
    private ObjectMapper objectMapper;

    private static Stream<Arguments> testGetStringArguments() {
        return Stream.of(
            Arguments.of("{\"data\": {\"test\": 2}}", "2"),
            Arguments.of("{\"data\": {\"test\": \"2\"}}", "2"),
            Arguments.of("{\"data\": {\"test\": \"2020-02-23\"}}", "2020-02-23")
        );
    }

    private static Stream<Arguments> testGetArguments() {
        return Stream.of(
            Arguments.of("{\"data\": {\"test\": \"2\"}}", Integer.class, 2),
            Arguments.of("{\"data\": {\"test\": \"2\"}}", String.class, "2"),
            Arguments.of("{\"data\": {\"test\": \"2\"}}", BigDecimal.class, new BigDecimal("2")),
            Arguments.of("{\"data\": {\"test\": \"2020-02-23\"}}", LocalDate.class, LocalDate.parse("2020-02-23")),
            Arguments.of("{\"data\": {\"test\": {\"foo\": \"fizzBuzz\", \"bar\": 13.8 }}}", FooBar.class,
                new FooBar("fizzBuzz", new BigDecimal("13.8")))
        );
    }

    private static Stream<Arguments> testGetListArguments() {
        return Stream.of(
            Arguments.of("{\"data\": {\"test\": [\"2\", \"1\"]}}", Integer.class, Arrays.asList(2, 1)),
            Arguments.of("{\"data\": {\"test\": [\"2\", \"1\"]}}", String.class, Arrays.asList("2", "1")),
            Arguments.of("{\"data\": {\"test\": [\"2\", \"1\"]}}", BigDecimal.class,
                Arrays.asList(new BigDecimal("2"), new BigDecimal("1"))),
            Arguments.of("{\"data\": {\"test\": [\"2020-02-23\", \"2020-02-24\"]}}", LocalDate.class,
                Arrays.asList(LocalDate.parse("2020-02-23"), LocalDate.parse("2020-02-24"))),
            Arguments.of("{\"data\":{\"test\":[{\"foo\":\"fizz\",\"bar\":1.23},{\"foo\":\"buzz\",\"bar\":32.12}]}}",
                FooBar.class,
                Arrays.asList(
                    new FooBar("fizz", new BigDecimal("1.23")),
                    new FooBar("buzz", new BigDecimal("32.12"))
                )
            )
        );
    }

    @DisplayName("Should get the JSON node's value as a String.")
    @ParameterizedTest
    @MethodSource("testGetStringArguments")
    public void testGetString(
        final String bodyString,
        final String expected
    ) {
        //GIVEN
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(bodyString), objectMapper);
        //WHEN
        final String actual = graphQLResponse.get(DATA_PATH);
        //THEN
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Should get the JSON node's value as an instance of a specified class.")
    @ParameterizedTest
    @MethodSource("testGetArguments")
    public <T> void testGet(
        final String bodyString,
        final Class<T> clazz,
        final T expected
    ) {
        //GIVEN
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(bodyString), objectMapper);
        //WHEN
        final T actual = graphQLResponse.get(DATA_PATH, clazz);
        //THEN
        assertThat(actual).isInstanceOf(clazz).isEqualTo(expected);
    }

    @DisplayName("Should get the JSON node's value as a List.")
    @ParameterizedTest
    @MethodSource("testGetListArguments")
    public <T> void testGetList(
        final String bodyString,
        final Class<T> clazz,
        final List<T> expected
    ) {
        //GIVEN
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(bodyString), objectMapper);
        //WHEN
        final List<T> actual = graphQLResponse.getList(DATA_PATH, clazz);
        //THEN
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("Should get field as defined by Jackson's JavaType.")
    @Test
    public void testGetAsJavaType() throws IOException {
        // GIVEN
        final String dataPath = "$.data.externalList[*].fooList";
        final String response = loadClassPathResource("response-with-nested-list.json");
        final List<List<String>> expected = Arrays.asList(Arrays.asList("foo1", "foo2"),
            Collections.singletonList("foo3"));
        final JavaType stringList = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        final JavaType listOfStringLists = objectMapper.getTypeFactory().constructCollectionType(List.class,
            stringList);
        // WHEN
        final List<List<String>> actual = new GraphQLResponse(ResponseEntity.ok(response), objectMapper)
            .get(dataPath, listOfStringLists);
        // THEN
        assertThat(actual).containsExactlyElementsOf(expected);

    }

    @DisplayName("Should throw illegal argument exception if type is incompatible")
    @Test
    public void testGetAsJavaTypeConversionError() throws IOException {
        // GIVEN
        final String dataPath = "$.data.externalList[*].fooList";
        final String response = loadClassPathResource("response-with-nested-list.json");
        final JavaType stringType = objectMapper.getTypeFactory().constructType(String.class);
        // WHEN
        final GraphQLResponse actual = new GraphQLResponse(ResponseEntity.ok(response), objectMapper);
        // THEN
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> actual.get(dataPath, stringType));

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"data\": { \"foo\":\"bar\" } }",
        "{\"errors\": null, \"data\": { \"foo\":\"bar\"} }",
        "{\"errors\": [], \"data\": { \"foo\":\"bar\"} }"
    })
    @DisplayName("Should pass the assertion if no errors are present.")
    void testExpectNoErrorsPass(final String response) {
        // WHEN
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(response), objectMapper);
        // THEN
        assertThat(graphQLResponse.assertThatNoErrorsArePresent())
            .as("Should not throw an exception. Should return GraphQL response instance.")
            .isSameAs(graphQLResponse);
    }

    @Test
    @DisplayName("Should throw assertion error if an error is present in the response.")
    void testExpectNoErrorsFail() {
        // WHEN
        final String response = "{\"errors\": [{\"message\": \"Test error.\"}], \"data\": { \"foo\":\"bar\"} }";
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(response), objectMapper);
        // THEN
        assertThatExceptionOfType(AssertionError.class)
            .isThrownBy(graphQLResponse::assertThatNoErrorsArePresent)
            .withMessage("Expected no GraphQL errors, but got 1: Test error.");
    }

    @Test
    @DisplayName("Should return assertion for the number of errors.")
    void testNumberOfErrorsAssertion() {
        // WHEN
        final String response = "{\"errors\": [{\"message\": \"Test error.\"}, {\"message\": \"Test error 2.\"}], "
            + "\"data\": { \"foo\":\"bar\"} }";
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(response), objectMapper);
        // THEN
        final NumberOfErrorsAssertion actual = graphQLResponse.assertThatNumberOfErrors();
        assertThat(actual).extracting("actual").isEqualTo(2);
        assertThat(actual.and()).isSameAs(graphQLResponse);
    }

    @Test
    @DisplayName("Should return assertion for the list of errors.")
    void testErrorListAssertion() {
        // GIVEN
        final String response = "{\"errors\": [{\"message\": \"Test error.\", \"errorType\": \"DataFetchingException\"}], "
            + "\"data\": { \"foo\":\"bar\"} }";
        // WHEN
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(response), objectMapper);
        final GraphQLErrorListAssertion actual = graphQLResponse.assertThatListOfErrors();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual).extracting("actual").isEqualTo(Collections.singletonList(GraphQLTestError.builder()
        .message("Test error.").errorType(ErrorType.DataFetchingException).build()));
        assertThat(actual.and()).isSameAs(graphQLResponse);
    }

    @Test
    @DisplayName("Should return a assertion for the json content of the response.")
    void testFieldAssertion() {
        // GIVEN
        final String response = "{ \"data\": { \"foo\":\"bar\"} }";
        // WHEN
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(response), objectMapper);
        final JsonContentAssert actual = graphQLResponse.assertThatJsonContent();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual).extracting("actual").isEqualTo(response);
    }

    @Test
    @DisplayName("Should return a assertion for the data field.")
    void testDataFieldAssertion() {
        // WHEN
        final GraphQLResponse response = new GraphQLResponse(ResponseEntity.ok("{}"), objectMapper);
        final GraphQLFieldAssert actual = response.assertThatDataField();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual).extracting("graphQLResponse", "jsonPath")
            .containsExactly(response, "$.data");
    }

    @Test
    @DisplayName("Should return a assertion for the errors field.")
    void testErrorFieldAssertion() {
        // WHEN
        final GraphQLResponse response = new GraphQLResponse(ResponseEntity.ok("{}"), objectMapper);
        final GraphQLFieldAssert actual = response.assertThatErrorsField();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual).extracting("graphQLResponse", "jsonPath")
            .containsExactly(response, "$.errors");
    }

    @Test
    @DisplayName("Should return a assertion for the extensions field.")
    void testErrorExtensionsAssertion() {
        // WHEN
        final GraphQLResponse response = new GraphQLResponse(ResponseEntity.ok("{}"), objectMapper);
        final GraphQLFieldAssert actual = response.assertThatExtensionsField();
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual).extracting("graphQLResponse", "jsonPath")
            .containsExactly(response, "$.extensions");
    }

    @Test
    @DisplayName("Should return a field assertion for the specific field.")
    void testJsonAssertion() {
        // GIVEN
        final String response = "{}";
        final String path = "$any.path";
        // WHEN
        final GraphQLResponse graphQLResponse = new GraphQLResponse(ResponseEntity.ok(response), objectMapper);
        final GraphQLFieldAssert actual = graphQLResponse.assertThatField(path);
        // THEN
        assertThat(actual).isNotNull();
        assertThat(actual).extracting("graphQLResponse", "jsonPath")
            .containsExactly(graphQLResponse, path);
    }

    private String loadClassPathResource(String path) throws IOException {
        try(InputStream resourceStream = new ClassPathResource(path).getInputStream()) {
            return StreamUtils.copyToString(resourceStream, StandardCharsets.UTF_8);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class FooBar {

        private String foo;
        private BigDecimal bar;
    }
}
