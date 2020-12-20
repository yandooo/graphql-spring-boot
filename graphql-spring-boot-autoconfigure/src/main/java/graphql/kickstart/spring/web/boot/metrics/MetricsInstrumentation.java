package graphql.kickstart.spring.web.boot.metrics;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Rodrigues
 */
public class MetricsInstrumentation extends TracingInstrumentation {

  private static final String QUERY_TIME_METRIC_NAME = "graphql.timer.query";
  private static final String RESOLVER_TIME_METRIC_NAME = "graphql.timer.resolver";
  private static final String OPERATION_NAME_TAG = "operationName";
  private static final String OPERATION = "operation";
  private static final String UNKNOWN_OPERATION_NAME = "unknown";
  private static final String PARENT = "parent";
  private static final String FIELD = "field";
  private static final String TIMER_DESCRIPTION = "Timer that records the time to fetch the data by Operation Name";
  private MeterRegistry meterRegistry;
  private Boolean tracingEnabled;

  public MetricsInstrumentation(MeterRegistry meterRegistry, Boolean tracingEnabled) {
    this.meterRegistry = meterRegistry;
    this.tracingEnabled = tracingEnabled;
  }

  @Override
  public CompletableFuture<ExecutionResult> instrumentExecutionResult(
      ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {

    if (executionResult.getExtensions() != null && executionResult.getExtensions()
        .containsKey("tracing")) {

      Map<String, Object> tracingData = (Map<String, Object>) executionResult.getExtensions()
          .get("tracing");
      Timer executionTimer = buildQueryTimer(parameters.getOperation(), "execution");
      executionTimer.record((long) tracingData.get("duration"), TimeUnit.NANOSECONDS);

      //These next 2 ifs might not run if the document is cached on the document provider
      if (tracingData.containsKey("validation") && ((Map<String, Object>) tracingData
          .get("validation")).containsKey("duration")) {
        Timer validationTimer = buildQueryTimer(parameters.getOperation(), "validation");
        validationTimer
            .record((long) ((Map<String, Object>) tracingData.get("validation")).get("duration"),
                TimeUnit.NANOSECONDS);
      }
      if (tracingData.containsKey("parsing") && ((Map<String, Object>) tracingData.get("parsing"))
          .containsKey("duration")) {
        Timer parsingTimer = buildQueryTimer(parameters.getOperation(), "parsing");
        parsingTimer
            .record((long) ((Map<String, Object>) tracingData.get("parsing")).get("duration"),
                TimeUnit.NANOSECONDS);
      }

      if (((Map<String, String>) tracingData.get("execution")).containsKey("resolvers")) {

        ((List<Map<String, Object>>) ((Map<String, Object>) tracingData.get("execution"))
            .get("resolvers")).forEach(field -> {

          Timer fieldTimer = buildFieldTimer(parameters.getOperation(), "resolvers",
              (String) field.get("parentType"), (String) field.get("fieldName"));
          fieldTimer.record((long) field.get("duration"), TimeUnit.NANOSECONDS);

        });

      }

      if (!tracingEnabled) {
        executionResult.getExtensions().remove("tracing");
      }
    }

    return CompletableFuture.completedFuture(executionResult);
  }

  private Timer buildQueryTimer(String operationName, String operation) {
    return Timer.builder(QUERY_TIME_METRIC_NAME)
        .description(TIMER_DESCRIPTION)
        .tag(OPERATION_NAME_TAG, operationName != null ? operationName : UNKNOWN_OPERATION_NAME)
        .tag(OPERATION, operation)
        .register(meterRegistry);
  }

  private Timer buildFieldTimer(String operationName, String operation, String parent,
      String field) {
    return Timer.builder(RESOLVER_TIME_METRIC_NAME)
        .description(TIMER_DESCRIPTION)
        .tag(OPERATION_NAME_TAG, operationName != null ? operationName : UNKNOWN_OPERATION_NAME)
        .tag(PARENT, parent)
        .tag(FIELD, field)
        .tag(OPERATION, operation)
        .register(meterRegistry);
  }
}
