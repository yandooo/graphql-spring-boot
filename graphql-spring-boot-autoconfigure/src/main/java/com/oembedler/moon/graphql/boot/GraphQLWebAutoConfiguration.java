/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 oEmbedler Inc. and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.oembedler.moon.graphql.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class})
@AutoConfigureAfter({GraphQLAutoConfiguration.class, WebMvcConfigurerAdapter.class})
public class GraphQLWebAutoConfiguration {

    private static final String DEFAULT_UPLOAD_MAX_FILE_SIZE = "128KB";
    private static final String DEFAULT_UPLOAD_MAX_REQUEST_SIZE = "128KB";

    @Value("${spring.graphql.server.mapping:/graphql}")
    private String graphQLServerMapping;

    @Bean
    @ConditionalOnProperty(value = "spring.graphql.server.corsEnabled", havingValue = "true", matchIfMissing = true)
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(graphQLServerMapping);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(GraphQLServerController.class)
    public GraphQLServerController basicGraphQLController() {
        return new GraphQLServerController();
    }

    @Bean
    @ConditionalOnMissingBean(GlobalDefaultExceptionHandler.class)
    @ConditionalOnProperty(value = "spring.graphql.server.suppressSpringResponseCodes", havingValue = "true", matchIfMissing = true)
    public GlobalDefaultExceptionHandler globalDefaultExceptionHandler() {
        return new GlobalDefaultExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(MultipartConfigElement.class)
    public MultipartConfigElement multipartConfigElement(GraphQLProperties graphQLProperties) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DEFAULT_UPLOAD_MAX_FILE_SIZE);
        factory.setMaxRequestSize(DEFAULT_UPLOAD_MAX_REQUEST_SIZE);

        String temp = graphQLProperties.getServer().getUploadMaxFileSize();
        if (StringUtils.hasText(temp))
            factory.setMaxFileSize(temp);

        temp = graphQLProperties.getServer().getUploadMaxRequestSize();
        if (StringUtils.hasText(temp))
            factory.setMaxRequestSize(temp);

        return factory.createMultipartConfig();
    }

}
