package graphql.kickstart.graphql.annotations.test.extend.type.model;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLTypeExtension;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Locale;

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
