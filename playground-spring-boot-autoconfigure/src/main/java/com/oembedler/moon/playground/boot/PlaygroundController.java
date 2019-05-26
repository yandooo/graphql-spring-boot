package com.oembedler.moon.playground.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaygroundController {

    private static final String CDN_ROOT = "https://cdn.jsdelivr.net/npm/graphql-playground-react";
    private static final String CDN_CSS = "build/static/css/index.css";
    private static final String CDN_FAVICON =  "build/favicon.png";
    private static final String CDN_SCRIPT = "build/static/js/middleware.js";
    private static final String CDN_LOGO = "build/logo.png";

    private static final String LOCAL_CSS = "/vendor/playground/playground.css";
    private static final String LOCAL_FAVICON = "/vendor/playground/favicon.png";
    private static final String LOCAL_SCRIPT = "/vendor/playground/playground.js";
    private static final String LOCAL_LOGO = "/vendor/playground/assets/logo.png";

    private static final String CSS_URL_ATTRIBUTE_NAME = "cssUrl";
    private static final String FAVICON_URL_ATTRIBUTE_NAME = "faviconUrl";
    private static final String SCRIPT_URL_ATTRIBUTE_NAME = "scriptUrl";
    private static final String LOGO_URL_ATTRIBUTE_NAME = "logoUrl";

    @Value("${playground.endpoint.graphql:/graphql}")
    private String graphqlEndpoint;

    @Value("${playground.endpoint.subscriptions:/subscriptions}")
    private String subscriptionsEndpoint;

    @Value("${playground.cdn.enabled:false}")
    private Boolean cdnEnabled;

    @Value("${playground.cdn.version:latest}")
    private String cdnVersion;

    @Value("${playground.pageTitle:Playground}")
    private String pageTitle;

    @GetMapping("${playground.mapping:/playground}")
    public String playground(final Model model) {
        if (cdnEnabled) {
            setCdnUrls(model);
        } else {
            setLocalAssetUrls(model);
        }
        model.addAttribute("graphqlEndpoint", graphqlEndpoint);
        model.addAttribute("subscriptionsEndpoint", subscriptionsEndpoint);
        model.addAttribute("pageTitle", pageTitle);
        return "playground";
    }

    private String getCdnUrl(final String assetUrl) {
        return String.format("%s@%s/%s", CDN_ROOT, cdnVersion, assetUrl);
    }

    private void setCdnUrls(final Model model) {
        model.addAttribute(CSS_URL_ATTRIBUTE_NAME, getCdnUrl(CDN_CSS));
        model.addAttribute(FAVICON_URL_ATTRIBUTE_NAME, getCdnUrl(CDN_FAVICON));
        model.addAttribute(SCRIPT_URL_ATTRIBUTE_NAME, getCdnUrl(CDN_SCRIPT));
        model.addAttribute(LOGO_URL_ATTRIBUTE_NAME, getCdnUrl(CDN_LOGO));
    }

    private void setLocalAssetUrls(final Model model) {
        model.addAttribute(CSS_URL_ATTRIBUTE_NAME, LOCAL_CSS);
        model.addAttribute(FAVICON_URL_ATTRIBUTE_NAME, LOCAL_FAVICON);
        model.addAttribute(SCRIPT_URL_ATTRIBUTE_NAME, LOCAL_SCRIPT);
        model.addAttribute(LOGO_URL_ATTRIBUTE_NAME, LOCAL_LOGO);
    }
}
