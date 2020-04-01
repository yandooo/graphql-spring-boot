package graphql.kickstart.spring.boot.graphql.annotations.example.model.directives;

import graphql.annotations.directives.AnnotationsDirectiveWiring;
import graphql.annotations.directives.AnnotationsWiringEnvironment;
import graphql.annotations.processor.util.CodeRegistryUtil;
import graphql.schema.GraphQLFieldDefinition;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpperCaseDirectiveWiring implements AnnotationsDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(final AnnotationsWiringEnvironment environment) {
        final GraphQLFieldDefinition field = (GraphQLFieldDefinition) environment.getElement();
        CodeRegistryUtil.wrapDataFetcher(field, environment, (((dataFetchingEnvironment, value) -> {
            if (value instanceof String) {
                return ((String) value).toUpperCase();
            }
            return value;
        })));
        return field;
    }
}
