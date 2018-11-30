package com.oembedler.moon.graphql.boot;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Rodrigues
 */
public class MetricsInstrumentation extends TracingInstrumentation {

    private MeterRegistry meterRegistry;

    private static final String OPERATION_TIME_METRIC_NAME = "graphql.timer.operation";
    private static final String OPERATION_NAME_TAG = "operationName";
    private static final String OPERATION = "operation";
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

                if (result.getExtensions().containsKey("tracingData")) {

                    Map<String, Object> tracingData = (Map<String, Object>) result.getExtensions().get("tracing");
                    Timer timer = buildTimer(parameters.getOperation(), "execution", t);
                    timer.record((long) tracingData.get("duration"), TimeUnit.NANOSECONDS);
                    timer.record((long) ((Map<String, Object>)tracingData.get("validation")).get("duration"), TimeUnit.NANOSECONDS);
                    timer.record((long) ((Map<String, Object>)tracingData.get("parsing")).get("duration"), TimeUnit.NANOSECONDS);

                }

            }

        };

    }

    private Timer buildTimer(String operationName, String operation, Throwable t) {
        return Timer.builder(OPERATION_TIME_METRIC_NAME)
                .description(TIMER_DESCRIPTION)
                .tag(OPERATION_NAME_TAG, operationName != null ? operationName : UNKNOWN_OPERATION_NAME)
                .tag(OPERATION, operation)
                .tag(EXCEPTION_TAG, t == null ? NONE_EXCEPTION : t.getClass().getSimpleName())
                .register(meterRegistry);
    }
}