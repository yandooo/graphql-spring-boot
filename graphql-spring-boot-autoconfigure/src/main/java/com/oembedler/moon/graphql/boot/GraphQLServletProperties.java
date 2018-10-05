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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@Configuration
@ConfigurationProperties(prefix = "graphql.servlet")
public class GraphQLServletProperties {

    private String mapping;

    private boolean asyncModeEnabled = false;

    public String getMapping() {
        return mapping != null ? mapping : "/graphql";
    }

    private boolean mappingIsServletWildcard() {
        return getMapping().endsWith("/*");
    }

    private boolean mappingIsAntWildcard() {
        return getMapping().endsWith("/**");
    }

    /**
     * @return the servlet mapping, coercing into an appropriate wildcard for servlets (ending in /*)
     */
    public String getServletMapping() {
        final String mapping = getMapping();
        if(mappingIsAntWildcard()) {
            return mapping.replaceAll("\\*$", "");
        } else if(mappingIsServletWildcard()) {
            return mapping;
        } else {
            return mapping.endsWith("/") ? mapping + "*" : mapping + "/*";
        }
    }

    /**
     * @return the servlet mapping, coercing into an appropriate wildcard for CORS, which uses ant matchers (ending in /**)
     */
    public String getCorsMapping() {
        final String mapping = getMapping();
        if(mappingIsAntWildcard()) {
            return mapping;
        } else if(mappingIsServletWildcard()) {
            return mapping + "*";
        } else {
            return mapping.endsWith("/") ? mapping + "**" : mapping + "/**";
        }
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public boolean isAsyncModeEnabled() {
        return asyncModeEnabled;
    }
}
