package graphql.kickstart.spring.boot.graphql.annotations.example.model.extension;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import graphql.annotations.annotationTypes.GraphQLTypeExtension;
import graphql.kickstart.spring.boot.graphql.annotations.example.model.type.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@GraphQLTypeExtension(Person.class)
public class PersonExtension {

    private final Person person;

    @GraphQLField
    @GraphQLNonNull
    public String fullName() {
        return String.format("%s, %s", person.getLastName(), person.getFirstName());
    }
}
