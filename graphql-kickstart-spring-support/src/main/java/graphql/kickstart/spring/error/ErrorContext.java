package graphql.kickstart.spring.error;

import graphql.language.SourceLocation;

import java.util.List;

public class ErrorContext {
    private final List<SourceLocation> locations;
    private final List<Object> path;

    public ErrorContext(List<SourceLocation> locations, List<Object> path) {
        this.locations = locations;
        this.path = path;
    }

    public List<SourceLocation> getLocations() {
        return locations;
    }

    public List<Object> getPath() {
        return path;
    }
}
