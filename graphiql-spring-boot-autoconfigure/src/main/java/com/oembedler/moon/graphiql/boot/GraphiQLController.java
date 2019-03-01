package com.oembedler.moon.graphiql.boot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Andrew Potter
 */
@Controller
public class GraphiQLController {

    private static final String CDNJS_CLOUDFLARE_COM_AJAX_LIBS = "//cdnjs.cloudflare.com/ajax/libs/";
    private static final String CDN_JSDELIVR_NET_NPM = "//cdn.jsdelivr.net/npm/";
    private static final String GRAPHIQL = "graphiql";

    @Value("${graphiql.endpoint.graphql:/graphql}")
    private String graphqlEndpoint;

    @Value("${graphiql.endpoint.subscriptions:/subscriptions}")
    private String subscriptionsEndpoint;

    @Value("${graphiql.static.basePath:/}")
    private String staticBasePath;

    @Value("${graphiql.pageTitle:GraphiQL}")
    private String pageTitle;

    @Value("${graphiql.cdn.enabled:false}")
    private Boolean graphiqlCdnEnabled;

    @Value("${graphiql.cdn.version:0.13.0}")
    private String graphiqlCdnVersion;

    @Value("${graphiql.subscriptions.timeout:30}")
    private int subscriptionsTimeout;

    @Autowired
    private Environment environment;

    private String template;
    private String props;
    private String headers;

    @PostConstruct
    public void onceConstructed() throws IOException {
        loadTemplate();
        loadProps();
        loadHeaders();
    }

    private void loadTemplate() throws IOException {
        try (InputStream inputStream = new ClassPathResource("graphiql.html").getInputStream()) {
            template = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        }
    }

    private void loadProps() throws IOException {
        props = new PropsLoader(environment).load();
    }

    private void loadHeaders() throws JsonProcessingException {
        PropertyGroupReader propertyReader = new PropertyGroupReader(environment, "graphiql.headers.");
        Properties headerProperties = propertyReader.load();
        addIfAbsent(headerProperties, "Accept");
        addIfAbsent(headerProperties, "Content-Type");
        this.headers = new ObjectMapper().writeValueAsString(headerProperties);
    }

    private void addIfAbsent(Properties headerProperties, String header) {
        if (!headerProperties.containsKey(header)) {
            headerProperties.setProperty(header, MediaType.APPLICATION_JSON_VALUE);
        }
    }

    @RequestMapping(value = "${graphiql.mapping:/graphiql}")
    public void graphiql(HttpServletRequest request, HttpServletResponse response, @PathVariable Map<String, String> params) throws IOException {
        response.setContentType("text/html; charset=UTF-8");

        Map<String, String> replacements = getReplacements(
                constructGraphQlEndpoint(request, params),
                request.getContextPath() + subscriptionsEndpoint,
                request.getContextPath() + staticBasePath
        );

        String populatedTemplate = StrSubstitutor.replace(template, replacements);
        response.getOutputStream().write(populatedTemplate.getBytes(Charset.defaultCharset()));
    }

    private Map<String, String> getReplacements(String graphqlEndpoint, String subscriptionsEndpoint, String staticBasePath) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("graphqlEndpoint", graphqlEndpoint);
        replacements.put("subscriptionsEndpoint", subscriptionsEndpoint);
        replacements.put("staticBasePath", staticBasePath);
        replacements.put("pageTitle", pageTitle);
        replacements.put("es6PromiseJsUrl", getResourceUrl(staticBasePath, "es6-promise.auto.min.js",
                joinCdnjsPath("es6-promise", "4.1.1", "es6-promise.auto.min.js")));
        replacements.put("fetchJsUrl", getResourceUrl(staticBasePath, "fetch.min.js",
                joinCdnjsPath("fetch", "2.0.4", "fetch.min.js")));
        replacements.put("reactJsUrl", getResourceUrl(staticBasePath, "react.min.js",
                joinCdnjsPath("react", "16.8.3", "umd/react.production.min.js")));
        replacements.put("reactDomJsUrl", getResourceUrl(staticBasePath, "react-dom.min.js",
                joinCdnjsPath("react-dom", "16.8.3", "umd/react-dom.production.min.js")));
        replacements.put("graphiqlCssUrl", getResourceUrl(staticBasePath, "graphiql.min.css",
                joinJsDelivrPath(GRAPHIQL, graphiqlCdnVersion, "graphiql.css")));
        replacements.put("graphiqlJsUrl", getResourceUrl(staticBasePath, "graphiql.min.js",
                joinJsDelivrPath(GRAPHIQL, graphiqlCdnVersion, "graphiql.min.js")));
        replacements.put("subscriptionsTransportWsBrowserClientUrl", getResourceUrl(staticBasePath,
                "subscriptions-transport-ws-browser-client.js",
                joinJsDelivrPath("subscriptions-transport-ws", "0.9.15", "browser/client.js")));
        replacements.put("graphiqlSubscriptionsFetcherBrowserClientUrl", getResourceUrl(staticBasePath,
                "graphiql-subscriptions-fetcher-browser-client.js",
                joinJsDelivrPath("graphiql-subscriptions-fetcher", "0.0.2", "browser/client.js")));
        replacements.put("props", props);
        replacements.put("headers", headers);
        replacements.put("subscriptionClientTimeout", String.valueOf(subscriptionsTimeout));
        return replacements;
    }

    private String getResourceUrl(String staticBasePath, String staticFileName, String cdnUrl) {
        if (graphiqlCdnEnabled && StringUtils.isNotBlank(cdnUrl)) {
            return cdnUrl;
        }
        return joinStaticPath(staticBasePath, staticFileName);
    }

    private String joinStaticPath(String staticBasePath, String staticFileName) {
        return staticBasePath + "vendor/" + staticFileName;
    }

    private String joinCdnjsPath(String library, String cdnVersion, String cdnFileName) {
        return CDNJS_CLOUDFLARE_COM_AJAX_LIBS + library + "/" + cdnVersion + "/" + cdnFileName;
    }

    private String joinJsDelivrPath(String library, String cdnVersion, String cdnFileName) {
        return CDN_JSDELIVR_NET_NPM + library + "@" + cdnVersion + "/" + cdnFileName;
    }

    private String constructGraphQlEndpoint(HttpServletRequest request, @RequestParam Map<String, String> params) {
        String endpoint = graphqlEndpoint;
        for (Map.Entry<String, String> param : params.entrySet()) {
            endpoint = endpoint.replaceAll("\\{" + param.getKey() + "}", param.getValue());
        }
        if (StringUtils.isNotBlank(request.getContextPath()) && !endpoint.startsWith(request.getContextPath())) {
            return request.getContextPath() + endpoint;
        }
        return endpoint;
    }

}
