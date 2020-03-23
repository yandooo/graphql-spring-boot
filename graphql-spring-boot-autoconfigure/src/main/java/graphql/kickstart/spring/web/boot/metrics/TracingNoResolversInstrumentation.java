package graphql.kickstart.spring.web.boot.metrics;

import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;

public class TracingNoResolversInstrumentation extends TracingInstrumentation {

    @Override
    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }
}
