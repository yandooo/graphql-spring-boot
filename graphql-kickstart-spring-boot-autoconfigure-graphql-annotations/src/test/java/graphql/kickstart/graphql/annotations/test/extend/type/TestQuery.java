package graphql.kickstart.graphql.annotations.test.extend.type;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.graphql.annotations.test.extend.type.model.BaseType;
import graphql.kickstart.tools.GraphQLQueryResolver;

public class TestQuery implements GraphQLQueryResolver {

    @GraphQLField
    public static BaseType someValue() {
        return new BaseType("Test value");
    }
}
