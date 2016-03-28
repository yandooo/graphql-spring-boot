package com.oembedler.moon.graphql.boot;

import com.google.common.collect.Maps;
import graphql.ErrorType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * @author <a href="mailto:java.lang.RuntimeException@gmail.com">oEmbedler Inc.</a>
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    public static final String ERROR_MESSAGE = "message";
    public static final String ERROR_TYPE = "type";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GraphQLServerResult> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // if the exception is annotated with @ResponseStatus rethrow it and let the framework handle it
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        Map<String, String> map = Maps.newHashMap();
        map.put(ERROR_MESSAGE, e.getMessage());
        map.put(ERROR_TYPE, ErrorType.ValidationError.toString());

        return ResponseEntity.ok(new GraphQLServerResult(Arrays.asList(map)));
    }
}