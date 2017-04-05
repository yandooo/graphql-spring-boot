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

    private static final String DEFAULT_UPLOAD_MAX_FILE_SIZE = "128KB";
    private static final String DEFAULT_UPLOAD_MAX_REQUEST_SIZE = "128KB";

    private String mapping;
    private String uploadMaxFileSize;
    private String uploadMaxRequestSize;

    public String getMapping() {
        return mapping != null ? mapping : "/graphql/*";
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getUploadMaxFileSize() {
        return uploadMaxFileSize != null ? uploadMaxFileSize : DEFAULT_UPLOAD_MAX_FILE_SIZE;
    }

    public void setUploadMaxFileSize(String uploadMaxFileSize) {
        this.uploadMaxFileSize = uploadMaxFileSize;
    }

    public String getUploadMaxRequestSize() {
        return uploadMaxRequestSize != null ? uploadMaxRequestSize : DEFAULT_UPLOAD_MAX_REQUEST_SIZE;
    }

    public void setUploadMaxRequestSize(String uploadMaxRequestSize) {
        this.uploadMaxRequestSize = uploadMaxRequestSize;
    }
}
