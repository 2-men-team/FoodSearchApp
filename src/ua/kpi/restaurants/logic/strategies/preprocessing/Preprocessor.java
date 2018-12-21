package ua.kpi.restaurants.logic.strategies.preprocessing;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Base interface for all query preprocessors.
 *
 * Preprocessor is a class that aims at performing additional operations
 * such as removal of useless words, spell checking various types of corrections etc.
 *
 * The main was to be able to perform all operations efficiently that is why lazy approach should pe preferred.
 *
 * This interface extends {@link Iterator} and {@link Iterable} interfaces to allow its convenient usage.
 */
public interface Preprocessor extends Iterator<String>, Iterable<String> {
  /**
   * Method, inherited from {@link Iterable} interface.
   * Provides convenient way to use instances of this interface in range-for loops.
   *
   * @return {@code this} instance
   */
  @NotNull
  @Override
  default Iterator<String> iterator() {
    return this;
  }

  /**
   * Creates {@link Stream} based on the current preprocessor state.
   *
   * @return {@link Stream} instance
   */
  @NotNull
  default Stream<String> asStream() {
    return StreamSupport.stream(spliterator(), false);
  }

  /**
   * Base interface for all preprocessor builders.
   *
   * This is usually implemented in the case when preprocessor class contains various components
   * and is hard to construct using constructors.
   */
  @FunctionalInterface
  interface Builder {
    /**
     * Produces {@code Preprocessor} instance for the given {@code query}.
     *
     * @param query to build an instance for (must not be {@code null})
     * @return initialized instance
     */
    @NotNull Preprocessor build(@NotNull String query);
  }
}
