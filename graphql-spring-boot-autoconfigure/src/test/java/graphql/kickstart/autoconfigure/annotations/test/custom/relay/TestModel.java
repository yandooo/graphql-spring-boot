package graphql.kickstart.autoconfigure.annotations.test.custom.relay;

import graphql.annotations.annotationTypes.GraphQLField;
import lombok.Value;

@Value
public class TestModel {

  @GraphQLField String someField;
}
