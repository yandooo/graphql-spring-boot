package com.oembedler.moon.graphiql.boot;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ReactiveGraphiQLController extends GraphiQLController {

  private DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

  @PostConstruct
  public void onceConstructed() throws IOException {
    super.onceConstructed();
  }

  @RequestMapping(value = "${graphiql.mapping:/graphiql}")
  public Mono<Void> graphiql(ServerHttpRequest request, ServerHttpResponse response,
                             @PathVariable Map<String, String> params) {
    response.getHeaders().setContentType(MediaType.TEXT_HTML);
    Object csrf = request.getQueryParams().getFirst("_csrf");
    return response.writeWith(Mono.just(request.getPath().contextPath().value())
        .map(contextPath -> super.graphiql(contextPath, params, csrf))
        .map(dataBufferFactory::wrap));
  }

}
