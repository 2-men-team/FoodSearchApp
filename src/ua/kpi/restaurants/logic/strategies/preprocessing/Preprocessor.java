package ua.kpi.restaurants.logic.strategies.preprocessing;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Preprocessor extends Iterator<String>, Iterable<String> {
  @NotNull
  @Override
  default Iterator<String> iterator() {
    return this;
  }

  @NotNull
  default Stream<String> asStream() {
    return StreamSupport.stream(spliterator(), false);
  }

  interface Builder {
    @NotNull Preprocessor build(@NotNull String query);
  }
}
