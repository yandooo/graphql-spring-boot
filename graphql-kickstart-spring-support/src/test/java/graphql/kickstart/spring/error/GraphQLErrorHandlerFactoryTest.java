package graphql.kickstart.spring.error;

import static java.util.Collections.singletonList;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    Mockito.when(applicationContext.getBeanFactory()).thenReturn(beanFactory);
    Mockito.when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[]{"Test"});
    Mockito.when(applicationContext.containsBean("Test")).thenReturn(true);
    Mockito.doReturn(TestClass.class).when(applicationContext).getType("Test");

    errorHandlerFactory = new GraphQLErrorHandlerFactory();
  }

  @Test
  public void createFindsCollectionHandler() {
    GraphQLErrorHandler handler = errorHandlerFactory.create(applicationContext, true);
    Assert.assertTrue(handler instanceof GraphQLErrorFromExceptionHandler);
    GraphQLErrorFromExceptionHandler errorHandler = (GraphQLErrorFromExceptionHandler) handler;
    Assert.assertFalse("handler.factories should not be empty", errorHandler.getFactories().isEmpty());
  }

  public static class TestClass {

    @ExceptionHandler(IllegalArgumentException.class)
    List<GraphQLError> handle(IllegalArgumentException e) {
      return singletonList(new ThrowableGraphQLError(e, "Illegal argument"));
    }

  }

}
