package com.oembedler.moon.graphql.boot;

import graphql.execution.instrumentation.InstrumentationState;

/**
 * @author Bruno Rodrigues
 */
public class MetricsSupport implements InstrumentationState {

    private final long startRequestNanos = System.nanoTime();

    public long getTotalTime() {
        return System.nanoTime() - startRequestNanos;
    }

}
