package com.graphql.spring.boot.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * An implementation of the {@link GraphQLError} interface for testing purposes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphQLTestError implements GraphQLError {
        private String message;
        private List<SourceLocation> locations;
        @JsonTypeInfo(defaultImpl = ErrorType.class, use = JsonTypeInfo.Id.CLASS)
        private ErrorClassification errorType;
        private List<Object> path;
        private Map<String, Object> extensions;
}
