package com.oembedler.moon.graphql.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Andrew Potter
 */
@Configuration
@ConfigurationProperties(prefix = "graphql.spring-graphql-common")
public class SpringGraphQLCommonProperties {
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
