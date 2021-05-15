package graphql.kickstart.autoconfigure.editor.altair;

import static java.lang.Integer.parseInt;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;

/** @author Moncef AOUDIA */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AltairController {

  private static final String CDN_JSDELIVR_NET_NPM = "//cdn.jsdelivr.net/npm/";
  private static final String ALTAIR = "altair-static";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AltairProperties altairProperties;
  private final AltairOptions altairOptions;
  private final AltairResources altairResources;

  private String template;

  @PostConstruct
  public void onceConstructed() throws IOException {
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    altairResources.load(altairOptions);
    loadTemplate();
  }

  private void loadTemplate() throws IOException {
    try (InputStream inputStream =
        new ClassPathResource("templates/altair.html").getInputStream()) {
      template = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
    }
  }

  @GetMapping(value = "${graphql.altair.mapping:/altair}")
  public void altair(HttpServletResponse response) throws IOException {
    response.setContentType("text/html; charset=UTF-8");
    String populatedTemplate = StringSubstitutor.replace(template, getReplacements());
    response.getOutputStream().write(populatedTemplate.getBytes(Charset.defaultCharset()));
  }

  @SneakyThrows
  private Map<String, String> getReplacements() {
    Map<String, String> replacements = new HashMap<>();
    replacements.put("pageTitle", altairProperties.getPageTitle());
    replacements.put("pageFavicon", getResourceUrl("favicon.ico", "favicon.ico"));
    replacements.put(
        "altairBaseUrl",
        getResourceUrl(
            StringUtils.join(altairProperties.getBasePath(), "/vendor/altair/"),
            joinJsdelivrPath(altairProperties.getCdn().getVersion())));
    replacements.put(
        "altairLogoUrl", getResourceUrl("assets/img/logo_350.svg", "assets/img/logo_350.svg"));
    replacements.put("altairCssUrl", getResourceUrl("styles.css", "styles.css"));

    val suffix = isJsSuffixAdded() ? "-es2018.js" : ".js";
    replacements.put("altairMainJsUrl", getResourceUrl("main-es2018.js", "main" + suffix));
    replacements.put(
        "altairPolyfillsJsUrl", getResourceUrl("polyfills-es2018.js", "polyfills" + suffix));
    replacements.put("altairRuntimeJsUrl", getResourceUrl("runtime-es2018.js", "runtime" + suffix));
    replacements.put("options", objectMapper.writeValueAsString(altairOptions));
    return replacements;
  }

  private boolean isJsSuffixAdded() {
    if (nonNull(altairProperties.getCdn().getVersion())) {
      String[] versionValues = altairProperties.getCdn().getVersion().split("\\.");
      return isNumeric(versionValues[0]) && parseInt(versionValues[0]) >= 4;
    }
    return false;
  }

  private String getResourceUrl(String staticFileName, String cdnUrl) {
    if (altairProperties.getCdn().isEnabled() && StringUtils.isNotBlank(cdnUrl)) {
      return cdnUrl;
    }
    return staticFileName;
  }

  private String joinJsdelivrPath(String cdnVersion) {
    return CDN_JSDELIVR_NET_NPM + AltairController.ALTAIR + "@" + cdnVersion + "/build/dist/";
  }
}
