package com.oembedler.moon.graphql.boot.error;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import graphql.GraphQLError;
import graphql.servlet.core.GraphQLErrorHandler;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RunWith(MockitoJUnitRunner.class)
public class GraphQLErrorHandlerFactoryTest {

  @Mock
  private ConfigurableApplicationContext applicationContext;
  @Mock
  private ConfigurableListableBeanFactory beanFactory;

  private GraphQLErrorHandlerFactory errorHandlerFactory;

  @Before
  public void setup() {
    when(applicationContext.getBeanFactory()).thenReturn(beanFactory);
    when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[]{"Test"});
    when(applicationContext.containsBean("Test")).thenReturn(true);
    doReturn(TestClass.class).when(applicationContext).getType("Test");

    errorHandlerFactory = new GraphQLErrorHandlerFactory();
  }

  @Test
  public void createFindsCollectionHandler() {
    GraphQLErrorHandler handler = errorHandlerFactory.create(applicationContext, true);
    assertTrue(handler instanceof GraphQLErrorFromExceptionHandler);
    GraphQLErrorFromExceptionHandler errorHandler = (GraphQLErrorFromExceptionHandler) handler;
    assertFalse("handler.factories should not be empty", errorHandler.getFactories().isEmpty());
  }

  public class TestClass {

    @ExceptionHandler(IllegalArgumentException.class)
    List<GraphQLError> handle(IllegalArgumentException e) {
      return singletonList(new ThrowableGraphQLError(e, "Illegal argument"));
    }
  }

}
