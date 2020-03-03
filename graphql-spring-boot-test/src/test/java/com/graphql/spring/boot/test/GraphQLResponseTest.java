package com.graphql.spring.boot.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class FooBar {
        private String foo;
        private BigDecimal bar;
    }
}
