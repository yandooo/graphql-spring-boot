package graphql.kickstart.voyager.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Max David Günther
 */
@Controller
public class VoyagerController {
    @Autowired
    private VoyagerIndexHtmlTemplate indexTemplate;

    @RequestMapping(value = "${voyager.mapping:/voyager}")
    public ResponseEntity<String> voyager(HttpServletRequest request) throws IOException {
        String contextPath = request.getContextPath();
        String indexHtmlContent = indexTemplate.fillIndexTemplate(contextPath);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/html; charset=UTF-8"))
                .body(indexHtmlContent);
    }
}
