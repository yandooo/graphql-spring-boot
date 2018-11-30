package com.oembedler.moon.graphql.boot.error;

import graphql.GraphQLError;
import graphql.servlet.DefaultGraphQLErrorHandler;
import graphql.servlet.GraphQLErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class GraphQLErrorHandlerFactory {

    public GraphQLErrorHandler create(ConfigurableApplicationContext applicationContext, boolean exceptionHandlersEnabled) {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        List<GraphQLErrorFactory> factories = Arrays.stream(beanFactory.getBeanDefinitionNames())
                .filter(applicationContext::containsBean)
                .map(beanFactory::getBeanDefinition)
                .map(BeanDefinition::getBeanClassName)
                .filter(Objects::nonNull)
                .map(name -> scanForExceptionHandlers(applicationContext, beanFactory, name))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (!factories.isEmpty() || exceptionHandlersEnabled) {
            return new GraphQLErrorFromExceptionHandler(factories);
        }

        return new DefaultGraphQLErrorHandler();
    }

    private List<GraphQLErrorFactory> scanForExceptionHandlers(ApplicationContext context, ConfigurableListableBeanFactory beanFactory, String className) {
        try {
            Class<?> objClz = beanFactory.getBeanClassLoader().loadClass(className);
            return Arrays.stream(objClz.getDeclaredMethods())
                    .filter(this::isGraphQLExceptionHandlerMethod)
                    .map(method -> GraphQLErrorFactory.withReflection(context.getBean(className), method))
                    .collect(Collectors.toList());
        } catch (ClassNotFoundException e) {
            log.error("Cannot load class " + className + ". " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private boolean isGraphQLExceptionHandlerMethod(Method method) {
        return method.isAnnotationPresent(ExceptionHandler.class) && GraphQLError.class.isAssignableFrom(method.getReturnType());
    }

}
