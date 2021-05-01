package graphql.kickstart.voyager.boot;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Guilherme Blanco
 */
public class VoyagerIndexHtmlTemplate {

  private static final String CDNJS_CLOUDFLARE_COM_AJAX_LIBS = "//cdnjs.cloudflare.com/ajax/libs/";
  private static final String CDN_JSDELIVR_NET_NPM = "//cdn.jsdelivr.net/npm/";
  private static final String VOYAGER = "graphql-voyager";
  private static final String FAVICON_APIS_GURU = "//apis.guru/graphql-voyager/icons/favicon-16x16.png";

  @Value("${voyager.endpoint:/graphql}")
  private String graphqlEndpoint;

  @Value("${voyager.pageTitle:Voyager}")
  private String pageTitle;

  @Value("${voyager.basePath:/}")
  private String basePath;

  @Value("${voyager.cdn.enabled:false}")
  private boolean voyagerCdnEnabled;

  @Value("${voyager.cdn.version:1.0.0-rc.31}")
  private String voyagerCdnVersion;

  @Value("${voyager.displayOptions.skipRelay:true}")
  private boolean voyagerDisplayOptionsSkipRelay;

  @Value("${voyager.displayOptions.skipDeprecated:true}")
  private boolean voyagerDisplayOptionsSkipDeprecated;

  @Value("${voyager.displayOptions.rootType:Query}")
  private String voyagerDisplayOptionsRootType;

  @Value("${voyager.displayOptions.sortByAlphabet:false}")
  private boolean voyagerDisplayOptionsSortByAlphabet;

  @Value("${voyager.displayOptions.showLeafFields:true}")
  private boolean voyagerDisplayOptionsShowLeafFields;

  @Value("${voyager.displayOptions.hideRoot:false}")
  private boolean voyagerDisplayOptionsHideRoot;

  @Value("${voyager.hideDocs:false}")
  private boolean voyagerHideDocs;

  @Value("${voyager.hideSettings:false}")
  private boolean voyagerHideSettings;

  public String fillIndexTemplate(String contextPath, Map<String, String> params)
      throws IOException {
    String template = StreamUtils
        .copyToString(new ClassPathResource("voyager.html").getInputStream(),
            Charset.defaultCharset());
    Map<String, String> replacements = new HashMap<>();
    replacements.put("graphqlEndpoint", constructGraphQlEndpoint(contextPath, params));
    replacements.put("pageTitle", pageTitle);
    replacements
        .put("pageFavicon", getResourceUrl(basePath, "favicon.ico", FAVICON_APIS_GURU));
    replacements.put("es6PromiseJsUrl", getResourceUrl(basePath, "es6-promise.auto.min.js",
        joinCdnjsPath("es6-promise", "4.1.1", "es6-promise.auto.min.js")));
    replacements.put("fetchJsUrl", getResourceUrl(basePath, "fetch.min.js",
        joinCdnjsPath("fetch", "2.0.4", "fetch.min.js")));
    replacements.put("reactJsUrl", getResourceUrl(basePath, "react.min.js",
        joinCdnjsPath("react", "16.8.3", "umd/react.production.min.js")));
    replacements.put("reactDomJsUrl", getResourceUrl(basePath, "react-dom.min.js",
        joinCdnjsPath("react-dom", "16.8.3", "umd/react-dom.production.min.js")));
    replacements.put("voyagerCssUrl", getResourceUrl(basePath, "voyager.css",
        joinJsDelivrPath(voyagerCdnVersion, "dist/voyager.css")));
    replacements.put("voyagerJsUrl", getResourceUrl(basePath, "voyager.min.js",
        joinJsDelivrPath(voyagerCdnVersion, "dist/voyager.min.js")));
    replacements.put("voyagerWorkerJsUrl", getResourceUrl(basePath, "voyager.worker.js",
        joinJsDelivrPath(voyagerCdnVersion, "dist/voyager.worker.min.js")));
    replacements.put("contextPath", contextPath);
    replacements.put("voyagerDisplayOptionsSkipRelay", Boolean.toString(voyagerDisplayOptionsSkipRelay));
    replacements.put("voyagerDisplayOptionsSkipDeprecated", Boolean.toString(voyagerDisplayOptionsSkipDeprecated));
    replacements.put("voyagerDisplayOptionsRootType", voyagerDisplayOptionsRootType);
    replacements.put("voyagerDisplayOptionsSortByAlphabet", Boolean.toString(voyagerDisplayOptionsSortByAlphabet));
    replacements.put("voyagerDisplayOptionsShowLeafFields", Boolean.toString(voyagerDisplayOptionsShowLeafFields));
    replacements.put("voyagerDisplayOptionsHideRoot", Boolean.toString(voyagerDisplayOptionsHideRoot));
    replacements.put("voyagerHideDocs", Boolean.toString(voyagerHideDocs));
    replacements.put("voyagerHideSettings", Boolean.toString(voyagerHideSettings));



    return StringSubstitutor.replace(template, replacements);
  }

  private String constructGraphQlEndpoint(String contextPath,
      @RequestParam Map<String, String> params) {
    String endpoint = graphqlEndpoint;
    for (Map.Entry<String, String> param : params.entrySet()) {
      endpoint = endpoint.replaceAll("\\{" + param.getKey() + "}", param.getValue());
    }
    if (StringUtils.isNotBlank(contextPath) && !endpoint.startsWith(contextPath)) {
      return contextPath + endpoint;
    }
    return endpoint;
  }

  private String getResourceUrl(String staticBasePath, String staticFileName, String cdnUrl) {
    if (voyagerCdnEnabled && StringUtils.isNotBlank(cdnUrl)) {
      return cdnUrl;
    }
    return joinStaticPath(staticBasePath, staticFileName);
  }

  private String joinStaticPath(String staticBasePath, String staticFileName) {
    return staticBasePath + "vendor/voyager/" + staticFileName;
  }

  private String joinCdnjsPath(String library, String cdnVersion, String cdnFileName) {
    return CDNJS_CLOUDFLARE_COM_AJAX_LIBS + library + "/" + cdnVersion + "/" + cdnFileName;
  }

  private String joinJsDelivrPath(String cdnVersion, String cdnFileName) {
    return CDN_JSDELIVR_NET_NPM + VoyagerIndexHtmlTemplate.VOYAGER + "@" + cdnVersion + "/" + cdnFileName;
  }
}
