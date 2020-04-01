package graphql.kickstart.spring.boot.graphql.annotations.example.model.type;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLID;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import graphql.kickstart.spring.boot.graphql.annotations.example.model.directives.UpperCaseDirective;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person {

    @Id
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    @UpperCaseDirective
    private String id;

    @Column(nullable = false)
    @GraphQLField
    @GraphQLNonNull
    private String firstName;

    @Column(nullable = false)
    @GraphQLField
    @GraphQLNonNull
    private String lastName;

    @GraphQLField
    private LocalDate dateOfBirth;
}
