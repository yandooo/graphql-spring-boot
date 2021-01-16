package graphql.kickstart.spring.webflux.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@SuppressWarnings("ALL")
@RequiredArgsConstructor
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MonoAutoConfigurationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void monoWrapper() throws IOException {
    val result = webTestClient.post()
        .uri("/graphql")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{ \"query\": \"query { hello } \"}")
        .exchange()
        .returnResult(String.class);
    val response = result.getResponseBody().blockFirst();
    assertThat(response).isEqualTo("{\"data\":{\"hello\":\"Hello world\"}}");
  }

}
