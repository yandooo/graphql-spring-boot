package com.oembedler.moon.graphql.boot;

import graphql.ExecutionResult;
import graphql.servlet.context.ContextSetting;
import graphql.servlet.core.GraphQLQueryInvoker;
import graphql.servlet.input.GraphQLSingleInvocationInput;
import lombok.Getter;
import org.springframework.lang.NonNull;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * This is a transactional wrapper for the default {@link GraphQLQueryInvoker} bean. The primary purpose of this class
 * is to prevent LazyInitializationException when working with nested
 * {@link com.coxautodev.graphql.tools.GraphQLResolver resolvers} and JPA entities. In these cases making the
 * individual resolvers {@link Transactional} may not be sufficient.
 *
 * Other than making the whole query invocation transactional, this wrapper does not change the behaviour of the
 * wrapped invoker, and will simply delegate all queries to it.
 *
 * To enable the transactional wrapper, set the {@code graphql.query-invoker.transactional} property to {@code true}
 * in application.properties/yaml.
 */
@Getter
@Transactional
public class TransactionalGraphQLQueryInvokerWrapper extends GraphQLQueryInvoker {
    //GraphQLQueryInvoker should be an interface...

    private @NonNull GraphQLQueryInvoker wrappedInvoker;

    /**
     * Constructor.
     * @param wrappedInvoker The wrapped query invoker. Must not be null.
     */
    public TransactionalGraphQLQueryInvokerWrapper(final @NonNull GraphQLQueryInvoker wrappedInvoker) {
        super(null, null, null, null);
        this.wrappedInvoker = Objects.requireNonNull(wrappedInvoker);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionResult query(final GraphQLSingleInvocationInput singleInvocationInput) {
        return wrappedInvoker.query(singleInvocationInput);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExecutionResult> query(final List<GraphQLSingleInvocationInput> batchedInvocationInput,
            final ContextSetting contextSetting) {
        return wrappedInvoker.query(batchedInvocationInput, contextSetting);
    }
}
