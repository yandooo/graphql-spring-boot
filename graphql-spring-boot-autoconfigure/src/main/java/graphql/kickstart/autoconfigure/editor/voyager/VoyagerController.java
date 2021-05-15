package graphql.kickstart.autoconfigure.editor.voyager;

import static graphql.kickstart.autoconfigure.editor.EditorConstants.CSRF_ATTRIBUTE_NAME;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;

/** @author Max David Günther */
@Controller
@RequiredArgsConstructor
public class VoyagerController {

  private final VoyagerIndexHtmlTemplate indexTemplate;

  @GetMapping(value = "${graphql.voyager.mapping:/voyager}")
  public ResponseEntity<String> voyager(
      HttpServletRequest request,
      final @RequestAttribute(value = CSRF_ATTRIBUTE_NAME, required = false) Object csrf,
      @PathVariable Map<String, String> params)
      throws IOException {
    String contextPath = request.getContextPath();
    String indexHtmlContent = indexTemplate.fillIndexTemplate(contextPath, csrf, params);
    return ResponseEntity.ok()
        .contentType(MediaType.valueOf("text/html; charset=UTF-8"))
        .body(indexHtmlContent);
  }
}
