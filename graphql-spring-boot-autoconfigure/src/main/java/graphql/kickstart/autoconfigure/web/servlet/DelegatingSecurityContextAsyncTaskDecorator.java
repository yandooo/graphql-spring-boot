package graphql.kickstart.autoconfigure.web.servlet;

import graphql.kickstart.servlet.AsyncTaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class DelegatingSecurityContextAsyncTaskDecorator implements AsyncTaskDecorator {

  @Override
  public Runnable decorate(Runnable runnable) {
    SecurityContext delegateSecurityContext = SecurityContextHolder.getContext();
    return () -> {
      SecurityContext originalSecurityContext = SecurityContextHolder.getContext();
      try {
        SecurityContextHolder.setContext(delegateSecurityContext);
        runnable.run();
      } finally {
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        if (emptyContext.equals(originalSecurityContext)) {
          SecurityContextHolder.clearContext();
        } else {
          SecurityContextHolder.setContext(originalSecurityContext);
        }
      }
    };
  }
}
