package graphql.kickstart.spring.boot.graphql.annotations.example.model.scalar;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Configuration
@NoArgsConstructor
public class LocalDateScalarConfiguration {

    @Bean
    public GraphQLScalarType localDateScalar() {
        return GraphQLScalarType.newScalar()
            .name("Date")
            .description("LocalDate as GraphQL scalar.")
            .coercing(new Coercing<LocalDate, String>() {
                @Override
                public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                    if (!(dataFetcherResult instanceof LocalDate)) {
                        throw new CoercingSerializeException("LocalDate expected.");
                    }
                    return String.valueOf(dataFetcherResult);
                }

                @Override
                public LocalDate parseValue(Object input) throws CoercingParseValueException {
                    try {
                        return LocalDate.parse(String.valueOf(input));
                    } catch (DateTimeParseException e) {
                        throw new CoercingParseValueException(e);
                    }
                }

                @Override
                public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
                    try {
                        if (!(input instanceof StringValue)) {
                            throw new CoercingParseLiteralException("String value expected.");
                        }
                        return LocalDate.parse(((StringValue) input).getValue());
                    } catch (DateTimeParseException e) {
                        throw new CoercingParseLiteralException("Failed to parse date literal", e);
                    }
                }
            })
            .build();
    }
}
