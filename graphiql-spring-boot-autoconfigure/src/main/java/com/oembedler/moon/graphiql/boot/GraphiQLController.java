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
    private static final String FETCH = "fetch";
    private static final String GRAPHIQL = "graphiql";
    private static final String ES_6_PROMISE = "es6-promise";
    private static final String REACT = "react";
    private static final String REACT_DOM = "react-dom";
    private static final String SUBSCRIPTIONS_TRANSPORT_WS = "subscriptions-transport-ws";
    private static final String GRAPHIQL_SUBSCRIPTIONS_FETCHER = "graphiql-subscriptions-fetcher";

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
        replacements.put("es6PromiseJsUrl", getCdnJsUrl(staticBasePath, graphiqlCdnEnabled, ES_6_PROMISE,
                "4.1.1", "es6-promise.auto.min.js", "es6-promise.auto.min.js"));
        replacements.put("fetchJsUrl", getCdnJsUrl(staticBasePath, graphiqlCdnEnabled, FETCH,
                "2.0.4", "fetch.min.js", "fetch.min.js"));
        replacements.put("reactJsUrl", getCdnJsUrl(staticBasePath, graphiqlCdnEnabled, REACT,
                "16.8.3", "umd/react.production.min.js", "react.min.js"));
        replacements.put("reactDomJsUrl", getCdnJsUrl(staticBasePath, graphiqlCdnEnabled, REACT_DOM,
                "16.8.3", "umd/react-dom.production.min.js", "react-dom.min.js"));
        replacements.put("graphiqlCssUrl", getJsDelivrUrl(staticBasePath, graphiqlCdnEnabled, GRAPHIQL,
                graphiqlCdnVersion, "graphiql.css", "graphiql.min.css"));
        replacements.put("graphiqlJsUrl", getJsDelivrUrl(staticBasePath, graphiqlCdnEnabled, GRAPHIQL,
                graphiqlCdnVersion, "graphiql.min.js", "graphiql.min.js"));
        replacements.put("subscriptionsTransportWsBrowserClient", getJsDelivrUrl(staticBasePath, graphiqlCdnEnabled,
                SUBSCRIPTIONS_TRANSPORT_WS, "0.9.15", "browser/client.js",
                "subscriptions-transport-ws-browser-client.js"));
        replacements.put("graphiqlSubscriptionsFetcherBrowserClient", getJsDelivrUrl(staticBasePath, graphiqlCdnEnabled,
                GRAPHIQL_SUBSCRIPTIONS_FETCHER, "0.0.2", "browser/client.js",
                "graphiql-subscriptions-fetcher-browser-client.js"));
        replacements.put("props", props);
        replacements.put("headers", headers);
        replacements.put("subscriptionClientTimeout", String.valueOf(subscriptionsTimeout));
        return replacements;
    }

    private String getCdnJsUrl(String staticBasePath, Boolean isCdnEnabled, String library,
                               String cdnVersion, String cdnFileName, String filename) {
        if (isCdnEnabled && StringUtils.isNotBlank(cdnVersion)) {
            return CDNJS_CLOUDFLARE_COM_AJAX_LIBS + library + "/" + cdnVersion + "/" + cdnFileName;
        }
        return staticBasePath + "vendor/" + filename;
    }

    private String getJsDelivrUrl(String staticBasePath, Boolean isCdnEnabled, String library,
                                  String cdnVersion, String cdnFileName, String filename) {
        if (isCdnEnabled && StringUtils.isNotBlank(cdnVersion)) {
            return CDN_JSDELIVR_NET_NPM + library + "@" + cdnVersion + "/" + cdnFileName;
        }
        return staticBasePath + "vendor/" + filename;
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
