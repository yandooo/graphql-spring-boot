package com.oembedler.moon.graphql.boot;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
public class GraphQLContext extends HashMap<String, Object> {

    public static final String KEY_FILE_UPLOAD = "file";
    public static final String KEY_HTTP_REQUEST = "http-request";

    public HttpServletRequest setHttpRequest(final HttpServletRequest request) {
        this.put(KEY_HTTP_REQUEST, request);
        return getHttpRequest();
    }

    public MultipartFile setUploadedFile(final MultipartFile file) {
        this.put(KEY_FILE_UPLOAD, file);
        return getUploadedFile();
    }

    public MultipartFile getUploadedFile() {
        return (MultipartFile) this.get(KEY_FILE_UPLOAD);
    }

    public HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) this.get(KEY_HTTP_REQUEST);
    }
}
