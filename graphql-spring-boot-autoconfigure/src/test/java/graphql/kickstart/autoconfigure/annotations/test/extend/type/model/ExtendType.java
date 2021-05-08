package graphql.kickstart.autoconfigure.annotations.test.extend.type.model;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLTypeExtension;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@GraphQLTypeExtension(BaseType.class)
public class ExtendType {

  BaseType baseType;

  @GraphQLField
  public String extendTypeField() {
    return baseType.getBaseTypeField().toUpperCase(Locale.ENGLISH);
  }
}
