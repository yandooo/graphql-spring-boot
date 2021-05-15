package graphql.kickstart.autoconfigure.editor.playground;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.autoconfigure.editor.playground.properties.PlaygroundProperties;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
@RequiredArgsConstructor
public class PlaygroundController {

  private static final String CDN_ROOT = "https://cdn.jsdelivr.net/npm/graphql-playground-react";
  private static final String CSS_PATH = "static/css/index.css";
  private static final String FAVICON_PATH = "favicon.png";
  private static final String SCRIPT_PATH = "static/js/middleware.js";
  private static final String LOGO_PATH = "logo.png";

  private static final String CSS_URL_ATTRIBUTE_NAME = "cssUrl";
  private static final String FAVICON_URL_ATTRIBUTE_NAME = "faviconUrl";
  private static final String SCRIPT_URL_ATTRIBUTE_NAME = "scriptUrl";
  private static final String LOGO_URL_ATTRIBUTE_NAME = "logoUrl";
  private static final String CSRF = "_csrf";

  private final PlaygroundProperties properties;

  private final ObjectMapper objectMapper;

  @GetMapping("${graphql.playground.mapping:/playground}")
  public ResponseEntity<String> playground(
      final @RequestAttribute(value = CSRF, required = false) Object csrf) throws IOException {
    String template =
        StreamUtils.copyToString(
            new ClassPathResource("templates/playground.html").getInputStream(),
            Charset.defaultCharset());
    Map<String, String> replacements = new HashMap<>();
    if (properties.getCdn().isEnabled()) {
      addCdnUrls(replacements);
    } else {
      addLocalAssetUrls(replacements);
    }
    replacements.put("pageTitle", properties.getPageTitle());
    replacements.put("properties", objectMapper.writeValueAsString(properties));
    if (nonNull(csrf)) {
      replacements.put(CSRF, objectMapper.writeValueAsString(csrf));
    } else {
      replacements.put(CSRF, "null");
    }
    return ResponseEntity.ok()
        .contentType(MediaType.valueOf("text/html; charset=UTF-8"))
        .body(StringSubstitutor.replace(template, replacements));
  }

  private String getCdnUrl(final String assetPath) {
    return String.format("%s@%s/build/%s", CDN_ROOT, properties.getCdn().getVersion(), assetPath);
  }

  private String getLocalUrl(final String assetPath) {
    return Paths.get(properties.getStaticPath().getBase(), assetPath).toString().replace('\\', '/');
  }

  private void addCdnUrls(final Map<String, String> replacements) {
    replacements.put(CSS_URL_ATTRIBUTE_NAME, getCdnUrl(CSS_PATH));
    replacements.put(FAVICON_URL_ATTRIBUTE_NAME, getCdnUrl(FAVICON_PATH));
    replacements.put(SCRIPT_URL_ATTRIBUTE_NAME, getCdnUrl(SCRIPT_PATH));
    replacements.put(LOGO_URL_ATTRIBUTE_NAME, getCdnUrl(LOGO_PATH));
  }

  private void addLocalAssetUrls(final Map<String, String> replacements) {
    replacements.put(CSS_URL_ATTRIBUTE_NAME, getLocalUrl(CSS_PATH));
    replacements.put(FAVICON_URL_ATTRIBUTE_NAME, getLocalUrl(FAVICON_PATH));
    replacements.put(SCRIPT_URL_ATTRIBUTE_NAME, getLocalUrl(SCRIPT_PATH));
    replacements.put(LOGO_URL_ATTRIBUTE_NAME, getLocalUrl(LOGO_PATH));
  }
}
