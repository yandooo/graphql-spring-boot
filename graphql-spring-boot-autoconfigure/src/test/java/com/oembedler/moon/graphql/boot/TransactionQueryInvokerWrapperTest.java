package com.oembedler.moon.graphql.boot;

import graphql.ExecutionResult;
import graphql.servlet.context.ContextSetting;
import graphql.servlet.core.GraphQLQueryInvoker;
import graphql.servlet.input.GraphQLSingleInvocationInput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TransactionQueryInvokerWrapperTest {

    @Mock
    private GraphQLQueryInvoker wrappedInvoker;

    private TransactionalGraphQLQueryInvokerWrapper wrapper;

    @Before
    public void setUp() {
        wrapper = new TransactionalGraphQLQueryInvokerWrapper(wrappedInvoker);
    }

    @Test
    public void shouldHaveTransactionalAnnotation() {
        assertThat(TransactionalGraphQLQueryInvokerWrapper.class.getAnnotation(Transactional.class)).isNotNull();
    }

    @Test
    public void shouldWrapExistingQueryInvokerWithSingleQuery() {
        //GIVEN
        final GraphQLSingleInvocationInput invocationInput = mock(GraphQLSingleInvocationInput.class);
        final ExecutionResult expectedExecutionResult = mock(ExecutionResult.class);
        given(wrappedInvoker.query(invocationInput)).willReturn(expectedExecutionResult);
        //WHEN
        final ExecutionResult actualExecutionResult = wrapper.query(invocationInput);
        //THEN
        then(wrappedInvoker).should().query(invocationInput);
        then(wrappedInvoker).shouldHaveNoMoreInteractions();
        assertThat(actualExecutionResult)
                .as("Should call the wrapped invoker, and return the execution result returned by it.")
                .isEqualTo(expectedExecutionResult);
    }

    @Test
    public void shouldWrapExistingQueryInvokerWithBatchedQuery() {
        //GIVEN
        final GraphQLSingleInvocationInput invocationInput = mock(GraphQLSingleInvocationInput.class);
        final List<GraphQLSingleInvocationInput> invocationInputList = Collections.singletonList(invocationInput);
        final ContextSetting contextSetting = ContextSetting.PER_QUERY_WITH_INSTRUMENTATION;
        final ExecutionResult expectedExecutionResult = mock(ExecutionResult.class);
        final List<ExecutionResult> expectedExecutionResultList = Collections.singletonList(expectedExecutionResult);
        given(wrappedInvoker.query(invocationInputList, contextSetting)).willReturn(expectedExecutionResultList);
        //WHEN
        final List<ExecutionResult> actualExecutionResultList = wrapper.query(invocationInputList, contextSetting);
        //THEN
        then(wrappedInvoker).should().query(invocationInputList, contextSetting);
        then(wrappedInvoker).shouldHaveNoMoreInteractions();
        assertThat(actualExecutionResultList)
                .as("Should call the wrapped invoker, and return the execution result list returned by it.")
                .isEqualTo(expectedExecutionResultList);
    }
}
