package com.oembedler.moon.graphql.boot;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.servlet.GraphQLContext;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Rodrigues
 */
public class MetricsInstrumentation extends SimpleInstrumentation {

    public InstrumentationState createState() {
        return new MetricsSupport();
    }

    private MeterRegistry meterRegistry;

    private static final String OPERATION_TIME_METRIC_NAME = "graphql.timer.operation";
    private static final String OPERATION_NAME_TAG = "operationName";
    private static final String UNKNOWN_OPERATION_NAME = "unknown";
    private static final String EXCEPTION_TAG = "exceptionName";
    private static final String NONE_EXCEPTION = "None";
    private static final String TIMER_DESCRIPTION = "Timer that records the time to fetch the data by Operation Name";

    public MetricsInstrumentation(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {

        return new SimpleInstrumentationContext<ExecutionResult>() {
            @Override
            public void onCompleted(ExecutionResult result, Throwable t) {

            GraphQLContext graphQLContext = parameters.getContext();
            MetricsSupport metricsSupport = parameters.getInstrumentationState();
            if (graphQLContext.getHttpServletRequest().isPresent()) {

                Timer timer = buildTimer(parameters.getOperation(), t);
                timer.record(metricsSupport.getTotalTime(), TimeUnit.NANOSECONDS);

            }

            }

        };

    }

    private Timer buildTimer(String operationName, Throwable t) {
        return Timer.builder(OPERATION_TIME_METRIC_NAME)
                .description(TIMER_DESCRIPTION)
                .tag(OPERATION_NAME_TAG, operationName != null ? operationName : UNKNOWN_OPERATION_NAME)
                .tag(EXCEPTION_TAG, t == null ? NONE_EXCEPTION : t.getClass().getSimpleName())
                .register(meterRegistry);
    }
}