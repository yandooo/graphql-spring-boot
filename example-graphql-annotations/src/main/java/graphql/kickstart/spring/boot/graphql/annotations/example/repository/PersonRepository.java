package graphql.kickstart.spring.boot.graphql.annotations.example.repository;

import graphql.kickstart.spring.boot.graphql.annotations.example.model.type.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
