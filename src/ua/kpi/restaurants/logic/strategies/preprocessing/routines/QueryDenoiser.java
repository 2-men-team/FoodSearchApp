package ua.kpi.restaurants.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default {@link Denoiser} implementation used throughout the application.
 *
 * It is aimed at lazy evaluation of the algorithm. It maintains a {@link Set} of noise words
 * which is typically initialized from {@link ua.kpi.restaurants.data.DataBase#getStopWords()}.
 *
 * @see ua.kpi.restaurants.data.DataBase
 */
public final class QueryDenoiser implements Denoiser {
  private final Set<String> stopWords;

  /**
   * Constructs denoiser from the {@link Set} of noise words.
   *
   * @param stopWords {@link Set} of noise words (must not be {@code null})
   */
  public QueryDenoiser(@NotNull Set<String> stopWords) {
    this.stopWords = stopWords;
  }

  /**
   * Checks whether given word is a noise.
   *
   * @param word to be checked (must not be {@code null})
   * @return {@code true} if word is a noise, {@code false} otherwise
   * @see Denoiser#isNoise(String)
   */
  @Override
  public boolean isNoise(@NotNull String word) {
    return stopWords.contains(word);
  }

  /**
   * Produces a {@link Stream} instance for the lazy evaluation of the algorithm.
   *
   * @param query - list of words to filter (must not be {@code null})
   * @return {@link Stream} instance
   * @see Denoiser#asStream(List)
   */
  @Override
  @NotNull
  public Stream<String> asStream(@NotNull List<String> query) {
    return query.stream().filter(word -> !isNoise(word));
  }

  /**
   * Applies denoising algorithm to the whole query removing noise words from it.
   *
   * @param query - list of words to filter (must not be {@code null})
   * @return filtered query
   * @see Denoiser#apply(List)
   */
  @Override
  @NotNull
  public List<String> apply(@NotNull List<String> query) {
    return asStream(query).collect(Collectors.toList());
  }
}
