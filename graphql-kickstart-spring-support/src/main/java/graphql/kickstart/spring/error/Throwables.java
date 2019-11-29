package graphql.kickstart.spring.error;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class Throwables {

    private List<Class<? extends Throwable>> throwables;

    Throwables(Class<? extends Throwable>[] throwables) {
        this.throwables = Arrays.asList(throwables);
    }

    Optional<Class<? extends Throwable>> mostConcrete(Throwable throwable) {
        return throwables.stream()
                .filter(t -> t.isAssignableFrom(throwable.getClass()))
                .min(new ThrowableComparator());
    }

}
