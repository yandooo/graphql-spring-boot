package graphql.kickstart.spring.error;

import graphql.ErrorClassification;
import graphql.language.SourceLocation;
import java.util.List;
import java.util.Map;
import lombok.Value;

@Value
public class ErrorContext {

  List<SourceLocation> locations;
  List<Object> path;
  Map<String, Object> extensions;
  ErrorClassification errorType;
}
