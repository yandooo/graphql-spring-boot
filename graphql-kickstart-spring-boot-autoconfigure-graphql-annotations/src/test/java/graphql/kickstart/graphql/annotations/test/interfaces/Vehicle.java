package graphql.kickstart.graphql.annotations.test.interfaces;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import graphql.annotations.annotationTypes.GraphQLTypeResolver;
import graphql.kickstart.graphql.annotations.GraphQLInterfaceTypeResolver;

@GraphQLTypeResolver(GraphQLInterfaceTypeResolver.class)
public interface Vehicle {

  @GraphQLField
  @GraphQLNonNull
  String getRegistrationNumber();
}
