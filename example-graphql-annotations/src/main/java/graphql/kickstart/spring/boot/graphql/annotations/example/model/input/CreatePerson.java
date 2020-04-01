package graphql.kickstart.spring.boot.graphql.annotations.example.model.input;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePerson {

    @GraphQLField
    @GraphQLNonNull
    private String firstName;

    @GraphQLField
    @GraphQLNonNull
    private String lastName;

    @GraphQLField
    private LocalDate dateOfBirth;
}
