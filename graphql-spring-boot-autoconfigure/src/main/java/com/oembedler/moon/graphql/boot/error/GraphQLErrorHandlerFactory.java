package com.oembedler.moon.graphql.boot.error;

import graphql.GraphQLError;
import graphql.servlet.DefaultGraphQLErrorHandler;
import graphql.servlet.GraphQLErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GraphQLErrorHandlerFactory {

    public GraphQLErrorHandler create(ConfigurableApplicationContext applicationContext, boolean exceptionHandlersEnabled) {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        List<GraphQLErrorFactory> factories = Arrays.stream(beanFactory.getBeanDefinitionNames())
                .filter(applicationContext::containsBean)
                .map(name -> scanForExceptionHandlers(applicationContext, name))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (!factories.isEmpty() || exceptionHandlersEnabled) {
            log.debug("Handle GraphQL errors using exception handlers defined in {} custom factories", factories.size());
            return new GraphQLErrorFromExceptionHandler(factories);
        }

        log.debug("Using default GraphQL error handler");
        return new DefaultGraphQLErrorHandler();
    }

    private List<GraphQLErrorFactory> scanForExceptionHandlers(ApplicationContext context, String name) {
        try {
            Class<?> objClz = context.getBean(name).getClass();
            return Arrays.stream(objClz.getDeclaredMethods())
                    .filter(this::isGraphQLExceptionHandlerMethod)
                    .map(method -> GraphQLErrorFactory.withReflection(context.getBean(name), method))
                    .collect(Collectors.toList());
        } catch (BeanCreationException e) {
            log.error("Cannot load class " + name + ". " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private boolean isGraphQLExceptionHandlerMethod(Method method) {
        return method.isAnnotationPresent(ExceptionHandler.class) && GraphQLError.class.isAssignableFrom(method.getReturnType());
    }

}
