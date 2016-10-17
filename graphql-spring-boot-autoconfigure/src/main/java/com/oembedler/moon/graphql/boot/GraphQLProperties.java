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
@ConfigurationProperties(prefix = "graphql")
public class GraphQLProperties {

    @NestedConfigurationProperty
    private Server server = new Server();

    @NestedConfigurationProperty
    private Schema schema = new Schema();

    public static class Server {
        private String mapping;
        private String queryKey;
        private String variablesKey;
        private String uploadMaxFileSize;
        private String uploadMaxRequestSize;
        private Boolean suppressSpringResponseCodes;

        public String getMapping() {
            return mapping;
        }

        public void setMapping(String mapping) {
            this.mapping = mapping;
        }

        public String getQueryKey() {
            return queryKey;
        }

        public void setQueryKey(String queryKey) {
            this.queryKey = queryKey;
        }

        public String getVariablesKey() {
            return variablesKey;
        }

        public void setVariablesKey(String variablesKey) {
            this.variablesKey = variablesKey;
        }

        public String getUploadMaxFileSize() {
            return uploadMaxFileSize;
        }

        public void setUploadMaxFileSize(String uploadMaxFileSize) {
            this.uploadMaxFileSize = uploadMaxFileSize;
        }

        public String getUploadMaxRequestSize() {
            return uploadMaxRequestSize;
        }

        public void setUploadMaxRequestSize(String uploadMaxRequestSize) {
            this.uploadMaxRequestSize = uploadMaxRequestSize;
        }

        public Boolean getSuppressSpringResponseCodes() {
            return suppressSpringResponseCodes;
        }

        public void setSuppressSpringResponseCodes(Boolean suppressSpringResponseCodes) {
            this.suppressSpringResponseCodes = suppressSpringResponseCodes;
        }
    }

    public static class Schema {
        private String clientMutationIdName;
        private Boolean injectClientMutationId;
        private Boolean allowEmptyClientMutationId;
        private String mutationInputArgumentName;
        private String outputObjectNamePrefix;
        private String inputObjectNamePrefix;
        private String schemaMutationObjectName;

        public String getClientMutationIdName() {
            return clientMutationIdName;
        }

        public void setClientMutationIdName(String clientMutationIdName) {
            this.clientMutationIdName = clientMutationIdName;
        }

        public Boolean getInjectClientMutationId() {
            return injectClientMutationId;
        }

        public void setInjectClientMutationId(Boolean injectClientMutationId) {
            this.injectClientMutationId = injectClientMutationId;
        }

        public Boolean getAllowEmptyClientMutationId() {
            return allowEmptyClientMutationId;
        }

        public void setAllowEmptyClientMutationId(Boolean allowEmptyClientMutationId) {
            this.allowEmptyClientMutationId = allowEmptyClientMutationId;
        }

        public String getMutationInputArgumentName() {
            return mutationInputArgumentName;
        }

        public void setMutationInputArgumentName(String mutationInputArgumentName) {
            this.mutationInputArgumentName = mutationInputArgumentName;
        }

        public String getOutputObjectNamePrefix() {
            return outputObjectNamePrefix;
        }

        public void setOutputObjectNamePrefix(String outputObjectNamePrefix) {
            this.outputObjectNamePrefix = outputObjectNamePrefix;
        }

        public String getInputObjectNamePrefix() {
            return inputObjectNamePrefix;
        }

        public void setInputObjectNamePrefix(String inputObjectNamePrefix) {
            this.inputObjectNamePrefix = inputObjectNamePrefix;
        }

        public String getSchemaMutationObjectName() {
            return schemaMutationObjectName;
        }

        public void setSchemaMutationObjectName(String schemaMutationObjectName) {
            this.schemaMutationObjectName = schemaMutationObjectName;
        }
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
}
