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
import java.util.Optional;

import static java.util.Objects.nonNull;

/**
 * An implementation of the {@link GraphQLError} interface for testing purposes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphQLTestError implements GraphQLError {
        private String message;
        @JsonTypeInfo(defaultImpl = JacksonFriendlySourceLocation.class, use = JsonTypeInfo.Id.CLASS)
        private List<SourceLocation> locations;
        @JsonTypeInfo(defaultImpl = ErrorType.class, use = JsonTypeInfo.Id.CLASS)
        private ErrorClassification errorType;
        private List<Object> path;
        private Map<String, Object> extensions;

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(Optional.of(message).orElse("Error without error message"));
            if (nonNull(locations) && !locations.isEmpty()) {
                sb.append(" at line ");
                locations.forEach(
                    location -> sb
                        .append(location.getLine())
                        .append(", column ")
                        .append(location.getColumn()).append(" in ")
                        .append(Optional.ofNullable(location.getSourceName()).orElse("unnamed/unspecified source."))
                );
            }
            return sb.toString();
        }
}
