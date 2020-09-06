package graphql.kickstart.graphql.annotations.test.extend.type;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.GraphQLQueryResolver;
import graphql.kickstart.graphql.annotations.test.extend.type.model.BaseType;

@GraphQLQueryResolver
public class TestQuery {

    @GraphQLField
    public static BaseType someValue() {
        return new BaseType("Test value");
    }
}
