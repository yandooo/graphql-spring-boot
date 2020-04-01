package graphql.kickstart.spring.boot.graphql.annotations.example;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import graphql.kickstart.spring.boot.graphql.annotations.example.model.type.Person;
import graphql.kickstart.spring.boot.graphql.annotations.example.repository.PersonRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PeopleQueryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @DisplayName("Should return all people.")
    void testPeopleQuery() throws IOException {
        // GIVEN
        final Person person1 = personRepository.save(Person.builder()
            .id(generateId())
            .firstName("John")
            .lastName("Doe")
            .dateOfBirth(LocalDate.parse("1900-01-02"))
            .build());
        final Person person2 = personRepository.save(Person.builder()
            .id(generateId())
            .firstName("Jane")
            .lastName("Doe")
            .dateOfBirth(LocalDate.parse("1900-02-01"))
            .build());
        // WHEN
        final GraphQLResponse graphQLResponse = graphQLTestTemplate.postForResource("people.graphql");
        final List<Person> actual = graphQLResponse.getList("$.data.people", Person.class);
        // THEN
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().containsExactly(person1, person2);
        assertThat(graphQLResponse.getList("$.data.people[*].fullName", String.class))
            .containsExactly("Doe, John", "Doe, Jane");
    }

    @NotNull
    private String generateId() {
        return String.valueOf(UUID.randomUUID()).toUpperCase(Locale.ENGLISH);
    }
}
