package com.oembedler.moon.graphiql.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("graphiql")
class GraphiQLProperties {

    private CodeMirror codeMirror = new CodeMirror();
    private Props props = new Props();

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
}
