package graphql.kickstart.autoconfigure.editor.altair;

import static java.lang.Integer.parseInt;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.kickstart.autoconfigure.editor.PropertyGroupReader;
import graphql.kickstart.autoconfigure.editor.PropsLoader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/** @author Moncef AOUDIA */
@Slf4j
@Controller
public class AltairController {

  private static final String CDN_JSDELIVR_NET_NPM = "//cdn.jsdelivr.net/npm/";
  private static final String ALTAIR = "altair-static";

  @Autowired private AltairProperties altairProperties;
  @Autowired private AltairOptions altairOptions;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired private Environment environment;

  private String template;
  private String props;
  private String headers;

  @PostConstruct
  public void onceConstructed() throws IOException {
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    loadTemplate();
    loadProps();
    loadHeaders();
  }

  private void loadTemplate() throws IOException {
    try (InputStream inputStream =
        new ClassPathResource("templates/altair.html").getInputStream()) {
      template = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
    }
  }

  private void loadProps() throws IOException {
    props = new PropsLoader(environment, "graphql.altair.props.resources.", "graphql.altair.props.variables.").load();
  }

  private void loadHeaders() throws JsonProcessingException {
    PropertyGroupReader propertyReader = new PropertyGroupReader(environment, "graphql.altair.headers.");
    Properties headerProperties = propertyReader.load();
    this.headers = new ObjectMapper().writeValueAsString(headerProperties);
  }

  @GetMapping(value = "${graphql.altair.mapping:/altair}")
  public void altair(
      HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable Map<String, String> params)
      throws IOException {
    response.setContentType("text/html; charset=UTF-8");

    Map<String, String> replacements =
        getReplacements(
            constructGraphQlEndpoint(request, params),
            request.getContextPath() + altairProperties.getEndpoint().getSubscriptions());

    String populatedTemplate = StringSubstitutor.replace(template, replacements);
    response.getOutputStream().write(populatedTemplate.getBytes(Charset.defaultCharset()));
  }

  @SneakyThrows
  private Map<String, String> getReplacements(
      String graphqlEndpoint, String subscriptionsEndpoint) {
    Map<String, String> replacements = new HashMap<>();
    replacements.put("graphqlEndpoint", graphqlEndpoint);
    replacements.put("subscriptionsEndpoint", subscriptionsEndpoint);
    replacements.put("pageTitle", altairProperties.getPageTitle());
    replacements.put("pageFavicon", getResourceUrl("favicon.ico", "favicon.ico"));
    replacements.put(
        "altairBaseUrl",
        getResourceUrl(
            StringUtils.join(altairProperties.getBasePath(), "/vendor/altair/"),
            joinJsUnpkgPath(ALTAIR, altairProperties.getCdn().getVersion(), "build/dist/")));
    replacements.put(
        "altairLogoUrl", getResourceUrl("assets/img/logo_350.svg", "assets/img/logo_350.svg"));
    replacements.put("altairCssUrl", getResourceUrl("styles.css", "styles.css"));

    val suffix = isJsSuffixAdded() ? "-es2018.js" : ".js";
    replacements.put("altairMainJsUrl", getResourceUrl("main-es2018.js", "main" + suffix));
    replacements.put("altairPolyfillsJsUrl", getResourceUrl("polyfills-es2018.js", "polyfills" + suffix));
    replacements.put("altairRuntimeJsUrl", getResourceUrl("runtime-es2018.js", "runtime" + suffix));
    replacements.put("props", props);
    replacements.put("options", objectMapper.writeValueAsString(altairOptions));
    replacements.put("headers", headers);
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

  private String joinJsUnpkgPath(String library, String cdnVersion, String cdnFileName) {
    return CDN_JSDELIVR_NET_NPM + library + "@" + cdnVersion + "/" + cdnFileName;
  }

  private String constructGraphQlEndpoint(
      HttpServletRequest request, @RequestParam Map<String, String> params) {
    String endpoint = altairProperties.getEndpoint().getGraphql();
    for (Map.Entry<String, String> param : params.entrySet()) {
      endpoint = endpoint.replaceAll("\\{" + param.getKey() + "}", param.getValue());
    }
    if (StringUtils.isNotBlank(request.getContextPath())
        && !endpoint.startsWith(request.getContextPath())) {
      return request.getContextPath() + endpoint;
    }
    return endpoint;
  }
}
