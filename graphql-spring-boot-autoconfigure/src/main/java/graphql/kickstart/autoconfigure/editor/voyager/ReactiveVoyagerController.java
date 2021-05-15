package graphql.kickstart.autoconfigure.editor.voyager;

import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** @author Max David Günther */
@Controller
@RequiredArgsConstructor
public class ReactiveVoyagerController {

  private final VoyagerIndexHtmlTemplate indexTemplate;

  @GetMapping(path = "${graphql.voyager.mapping:/voyager}")
  public Mono<ResponseEntity<String>> voyager(
      ServerWebExchange exchange, @PathVariable Map<String, String> params) {
    // no context path in spring-webflux
    Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());
    return csrfToken != null
        ? csrfToken.map(csrf -> fillTemplate(csrf, params))
        : Mono.just(fillTemplate(null, params));
  }

  private ResponseEntity<String> fillTemplate(CsrfToken csrf, Map<String, String> params) {
    try {
      String indexHtmlContent = indexTemplate.fillIndexTemplate("", csrf, params);
      return ResponseEntity.ok()
          .contentType(MediaType.valueOf("text/html; charset=UTF-8"))
          .body(indexHtmlContent);
    } catch (IOException e) {
      return ResponseEntity.status(500).body(null);
    }
  }
}
