package com.oembedler.moon.voyager.boot;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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

    @Value("${voyager.static.basePath:/}")
    private String staticBasePath;

    @Value("${voyager.cdn.enabled:false}")
    private Boolean voyagerCdnEnabled;

    @Value("${voyager.cdn.version:1.0.0-rc.26}")
    private String voyagerCdnVersion;

    public String fillIndexTemplate(String contextPath) throws IOException {
        String template = StreamUtils.copyToString(new ClassPathResource("voyager.html").getInputStream(), Charset.defaultCharset());
        Map<String, String> replacements = new HashMap<>();
        replacements.put("graphqlEndpoint", contextPath + graphqlEndpoint);
        replacements.put("pageTitle", pageTitle);
        replacements.put("pageFavicon", getResourceUrl(staticBasePath, "favicon.ico", FAVICON_APIS_GURU));
        replacements.put("es6PromiseJsUrl", getResourceUrl(staticBasePath, "es6-promise.auto.min.js",
                joinCdnjsPath("es6-promise", "4.1.1", "es6-promise.auto.min.js")));
        replacements.put("fetchJsUrl", getResourceUrl(staticBasePath, "fetch.min.js",
                joinCdnjsPath("fetch", "2.0.4", "fetch.min.js")));
        replacements.put("reactJsUrl", getResourceUrl(staticBasePath, "react.min.js",
                joinCdnjsPath("react", "16.8.3", "umd/react.production.min.js")));
        replacements.put("reactDomJsUrl", getResourceUrl(staticBasePath, "react-dom.min.js",
                joinCdnjsPath("react-dom", "16.8.3", "umd/react-dom.production.min.js")));
        replacements.put("voyagerCssUrl", getResourceUrl(staticBasePath, "voyager.css",
                joinJsDelivrPath(VOYAGER, voyagerCdnVersion, "dist/voyager.css")));
        replacements.put("voyagerJsUrl", getResourceUrl(staticBasePath, "voyager.min.js",
                joinJsDelivrPath(VOYAGER, voyagerCdnVersion, "dist/voyager.min.js")));
        replacements.put("voyagerWorkerJsUrl", getResourceUrl(staticBasePath, "voyager.worker.js",
                joinJsDelivrPath(VOYAGER, voyagerCdnVersion, "dist/voyager.worker.min.js")));
        replacements.put("contextPath", contextPath);

        return StrSubstitutor.replace(template, replacements);
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

    private String joinJsDelivrPath(String library, String cdnVersion, String cdnFileName) {
        return CDN_JSDELIVR_NET_NPM + library + "@" + cdnVersion + "/" + cdnFileName;
    }
}
