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
public class VoyagerController {

    private static final String CDNJS_CLOUDFLARE_COM_AJAX_LIBS = "//cdnjs.cloudflare.com/ajax/libs/";
    private static final String CDN_JSDELIVR_NET_NPM = "//cdn.jsdelivr.net/npm/";
    private static final String FETCH = "fetch";
    private static final String ES_6_PROMISE = "es6-promise";
    private static final String REACT = "react";
    private static final String REACT_DOM = "react-dom";
    private static final String VOYAGER = "graphql-voyager";

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

    @RequestMapping(value = "${voyager.mapping:/voyager}")
    public void voyager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();
        response.setContentType("text/html; charset=UTF-8");

        String template = StreamUtils.copyToString(new ClassPathResource("voyager.html").getInputStream(), Charset.defaultCharset());
        Map<String, String> replacements = new HashMap<>();
        replacements.put("graphqlEndpoint", contextPath + graphqlEndpoint);
        replacements.put("pageTitle", pageTitle);
        replacements.put("es6PromiseJsUrl", getCdnJsUrl(staticBasePath, voyagerCdnEnabled, ES_6_PROMISE,
                "4.1.1", "es6-promise.auto.min.js", "es6-promise.auto.min.js"));
        replacements.put("fetchJsUrl", getCdnJsUrl(staticBasePath, voyagerCdnEnabled, FETCH,
                "2.0.4", "fetch.min.js", "fetch.min.js"));
        replacements.put("reactJsUrl", getCdnJsUrl(staticBasePath, voyagerCdnEnabled, REACT,
                "16.8.3", "umd/react.production.min.js", "react.min.js"));
        replacements.put("reactDomJsUrl", getCdnJsUrl(staticBasePath, voyagerCdnEnabled, REACT_DOM,
                "16.8.3", "umd/react-dom.production.min.js", "react-dom.min.js"));
        replacements.put("voyagerCssUrl", getJsDeliverUrl(staticBasePath, voyagerCdnEnabled, VOYAGER,
                voyagerCdnVersion, "dist/voyager.css", "voyager.css"));
        replacements.put("voyagerJsUrl", getJsDeliverUrl(staticBasePath, voyagerCdnEnabled, VOYAGER,
                voyagerCdnVersion, "dist/voyager.min.js", "voyager.min.js"));
        replacements.put("voyagerWorkerJsUrl", getJsDeliverUrl(staticBasePath, voyagerCdnEnabled, VOYAGER,
                voyagerCdnVersion, "dist/voyager.worker.min.js", "voyager.worker.min.js"));
        replacements.put("contextPath", contextPath);

        response.getOutputStream().write(StrSubstitutor.replace(template, replacements).getBytes(Charset.defaultCharset()));
    }

    private String getCdnJsUrl(String staticBasePath, Boolean isCdnEnabled, String library,
                               String cdnVersion, String cdnFileName, String filename) {
        if (isCdnEnabled && StringUtils.isNotBlank(cdnVersion)) {
            return CDNJS_CLOUDFLARE_COM_AJAX_LIBS + library + "/" + cdnVersion + "/" + cdnFileName;
        }
        return staticBasePath + "vendor/" + filename;
    }

    private String getJsDeliverUrl(String staticBasePath, Boolean isCdnEnabled, String library,
                                   String cdnVersion, String cdnFileName, String filename) {
        if (isCdnEnabled && StringUtils.isNotBlank(cdnVersion)) {
            return CDN_JSDELIVR_NET_NPM + library + "@" + cdnVersion + "/" + cdnFileName;
        }
        return staticBasePath + "vendor/" + filename;
    }
}
