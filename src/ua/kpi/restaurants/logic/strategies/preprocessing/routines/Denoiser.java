package ua.kpi.restaurants.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Base interface for denoiser implementation.
 *
 * It is needed for {@link ua.kpi.restaurants.logic.strategies.preprocessing.QueryPreprocessor} to be used
 * as an operation during preprocessing. It aims at providing a unified interface for denoising operation.
 */
public interface Denoiser extends UnaryOperator<List<String>> {
  /** Dummy denoiser that does nothing */
  Denoiser DUMMY = new Denoiser() {
    @Override
    public boolean isNoise(@NotNull String word) {
      return false;
    }

    @NotNull
    @Override
    public Stream<String> asStream(@NotNull List<String> query) {
      return query.stream();
    }

    @NotNull
    @Override
    public List<String> apply(@NotNull List<String> query) {
      return query;
    }
  };

  /**
   * Indicates whether the given word is noise or not.
   *
   * @param word to be checked (must not be {@code null})
   * @return {@code true} if word is a noise, {@code false} otherwise
   */
  boolean isNoise(@NotNull String word);

  /**
   * Applies denoising algorithm to the list of words removing noise from it.
   *
   * @param query - list of words to filter (must not be {@code null})
   * @return filtered result
   */
  @NotNull List<String> apply(@NotNull List<String> query);

  /**
   * Performs lazy evaluation of the algorithm producing a {@link Stream} of valid words.
   *
   * @param query - list of words to filter (must not be {@code null})
   * @return {@link Stream} instance for lazy evaluation
   */
  @NotNull Stream<String> asStream(@NotNull List<String> query);
}
