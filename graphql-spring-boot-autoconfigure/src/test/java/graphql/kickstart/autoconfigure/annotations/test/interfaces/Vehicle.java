package graphql.kickstart.autoconfigure.annotations.test.interfaces;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import graphql.annotations.annotationTypes.GraphQLTypeResolver;
import graphql.kickstart.autoconfigure.annotations.GraphQLInterfaceTypeResolver;

@GraphQLTypeResolver(GraphQLInterfaceTypeResolver.class)
public interface Vehicle {

  @GraphQLField
  @GraphQLNonNull
  String getRegistrationNumber();
}
