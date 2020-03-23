package graphql.kickstart.voyager.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * @author Max David Günther
 */
@Controller
public class ReactiveVoyagerController {
    @Autowired
    private VoyagerIndexHtmlTemplate indexTemplate;

    @GetMapping(path = "${voyager.mapping:/voyager}")
    public ResponseEntity<String> voyager() throws IOException {
        // no context path in spring-webflux
        String indexHtmlContent = indexTemplate.fillIndexTemplate("");
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/html; charset=UTF-8"))
                .body(indexHtmlContent);
    }
}
