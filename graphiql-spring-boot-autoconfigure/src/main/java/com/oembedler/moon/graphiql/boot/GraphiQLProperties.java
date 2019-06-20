package com.oembedler.moon.graphiql.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties("graphiql")
class GraphiQLProperties {

    private Endpoint endpoint = new Endpoint();
    private Static STATIC = new Static();
    private CodeMirror codeMirror = new CodeMirror();
    private Props props = new Props();
    private String pageTitle = "GraphiQL";
    private String mapping = "/graphiql";
    private Subscriptions subscriptions = new Subscriptions();
    private Cdn cdn = new Cdn();

    @Data
    static class Endpoint {
        private String graphql = "/graphql";
        private String subscriptions = "/subscriptions";
    }

    @Data
    static class Static {
        private String basePath = "/";
    }

    @Data
    static class CodeMirror {
        private String version = "5.47.0";
    }

    @Data
    static class Props {

        private Variables variables = new Variables();

        @Data
        static class Variables {
            private String editorTheme;
        }
    }

    @Data
    static class Cdn {
        private boolean enabled = false;
        private String version = "0.13.0";
    }

    @Data
    static class Subscriptions {
        private int timeout = 30;
        private boolean reconnect = false;
    }
}
