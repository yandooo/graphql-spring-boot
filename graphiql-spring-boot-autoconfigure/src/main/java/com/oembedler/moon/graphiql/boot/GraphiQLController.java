package com.oembedler.moon.graphiql.boot;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Potter
 */
@Controller
public class GraphiQLController {

    @Value("${graphiql.endpoint:/graphql}")
    private String graphqlEndpoint;

    @Value("${graphiql.pageTitle:GraphiQL}")
    private String pageTitle;

    @Value("${graphiql.cdn.enabled:false}")
    private Boolean graphiqlCdnEnabled;

    @Value("${graphiql.cdn.version:0.11.11}")
    private String graphiqlCdnVersion;

    private String template;

    @PostConstruct
    public void loadTemplate() throws IOException {
        try (InputStream inputStream = new ClassPathResource("graphiql.html").getInputStream()) {
            template = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        }
    }

    @RequestMapping(value = "${graphiql.mapping:/graphiql}")
    public void graphiql(HttpServletResponse response, @PathVariable Map<String, String> params) throws IOException {
        response.setContentType("text/html; charset=UTF-8");

        Map<String, String> replacements = new HashMap<>();

        String graphiqlCssUrl = "/vendor/graphiql.min.css";
        String graphiqlJsUrl = "/vendor/graphiql.min.js";

        if (graphiqlCdnEnabled && !StringUtils.isEmpty(graphiqlCdnVersion)) {
            graphiqlCssUrl = "//cdnjs.cloudflare.com/ajax/libs/graphiql/" + graphiqlCdnVersion + "/graphiql.min.css";
            graphiqlJsUrl = "//cdnjs.cloudflare.com/ajax/libs/graphiql/" + graphiqlCdnVersion + "/graphiql.min.js";
        }

        String endpoint = constructGraphQlEndpoint(params);

        replacements.put("graphqlEndpoint", endpoint);
        replacements.put("pageTitle", pageTitle);
        replacements.put("graphiqlCssUrl", graphiqlCssUrl);
        replacements.put("graphiqlJsUrl", graphiqlJsUrl);

        response.getOutputStream().write(StrSubstitutor.replace(template, replacements).getBytes(Charset.defaultCharset()));
    }

    private String constructGraphQlEndpoint(@RequestParam Map<String, String> params) {
        String endpoint = graphqlEndpoint;
        for (Map.Entry<String, String> param : params.entrySet()) {
            endpoint = endpoint.replaceAll("\\{" + param.getKey() + "}", param.getValue());
        }
        return endpoint;
    }
}
