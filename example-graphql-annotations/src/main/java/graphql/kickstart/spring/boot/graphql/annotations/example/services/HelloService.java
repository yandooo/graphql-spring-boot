package graphql.kickstart.spring.boot.graphql.annotations.example.services;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String sayHello(final String whom) {
        return String.format("Hello, %s!", whom);
    }
}
