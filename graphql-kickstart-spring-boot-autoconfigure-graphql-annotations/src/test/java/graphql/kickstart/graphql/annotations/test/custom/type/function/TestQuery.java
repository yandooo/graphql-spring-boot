package graphql.kickstart.graphql.annotations.test.custom.type.function;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.kickstart.tools.GraphQLQueryResolver;

public class TestQuery implements GraphQLQueryResolver {

    @GraphQLField
    public static Foo foo(){
        return new Foo();
    }
}
