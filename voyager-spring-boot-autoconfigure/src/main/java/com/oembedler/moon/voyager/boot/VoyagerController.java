package com.oembedler.moon.voyager.boot;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Guilherme Blanco
 */
@Controller
public class VoyagerController
{
    @Value("${voyager.endpoint:/graphql}")
    private String graphqlEndpoint;

    @Value("${voyager.pageTitle:Voyager}")
    private String pageTitle;

    @Value("${voyager.cdn.enabled:false}")
    private Boolean voyagerCdnEnabled;

    @Value("${voyager.cdn.version:v1.x}")
    private String voyagerCdnVersion;

    @RequestMapping(value = "${voyager.mapping:/voyager}")
    public void voyager(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String contextPath = request.getContextPath();
        response.setContentType("text/html; charset=UTF-8");

        String voyagerCssUrl = contextPath + "/vendor/voyager.css";
        String voyagerJsUrl = contextPath + "/vendor/voyager.min.js";

        if (voyagerCdnEnabled && StringUtils.isNotBlank(voyagerCdnVersion)) {
            voyagerCssUrl = "//apis.guru/graphql-voyager/releases/" + voyagerCdnVersion + "/voyager.css";
            voyagerJsUrl = "//apis.guru/graphql-voyager/releases/" + voyagerCdnVersion + "/voyager.min.js";
        }

        String template = StreamUtils.copyToString(new ClassPathResource("voyager.html").getInputStream(), Charset.defaultCharset());
        Map<String, String> replacements = new HashMap<>();
        replacements.put("graphqlEndpoint", contextPath + graphqlEndpoint);
        replacements.put("pageTitle", pageTitle);
        replacements.put("voyagerCssUrl", voyagerCssUrl);
        replacements.put("voyagerJsUrl", voyagerJsUrl);
        replacements.put("contextPath", contextPath);

        response.getOutputStream().write(StrSubstitutor.replace(template, replacements).getBytes(Charset.defaultCharset()));
    }
}
