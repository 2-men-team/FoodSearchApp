package project.logic.strategies.preprocessing;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Preprocessor extends Iterator<String> {
    @NotNull
    default Stream<String> asStream() {
        Iterable<String> iterable = () -> this;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    interface Builder {
        @NotNull Preprocessor build(@NotNull String query);
    }
}
