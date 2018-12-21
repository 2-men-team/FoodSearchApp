package ua.kpi.restaurants.logic.strategies.preprocessing;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Denoiser;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Preprocessor used throughout the application.
 *
 * It was designed to simulate the following pipeline:
 * <ol>
 *   <li>query is divided by words using defined {@code delimiters}</li>
 *   <li>word is selected that is not noise and is not filtered out</li>
 *   <li>selected word is stemmed</li>
 *   <li>selected word is checked for correct spell checking and corrected if needed</li>
 * </ol>
 *
 * Instantiation is done using {@link QueryPreprocessor.Builder}.
 *
 * @see ua.kpi.restaurants.logic.strategies.preprocessing.routines.QueryDenoiser
 * @see ua.kpi.restaurants.logic.strategies.preprocessing.routines.Stemmer
 * @see ua.kpi.restaurants.logic.strategies.preprocessing.routines.SpellingCorrector
 */
public final class QueryPreprocessor implements Preprocessor {
  /** Default delimiters */
  @Language("RegExp")
  public static final String DELIMITERS = "(\\s|\\p{Punct}|[«»№])+";

  private final Predicate<String> predicate;
  private final Function<? super String, String> mapper;
  private final Queue<String> queue;

  private QueryPreprocessor(
      String delimiters,
      Predicate<String> predicate,
      Function<? super String, String> mapper,
      String query
  ) {
    this.predicate = predicate;
    this.mapper = mapper;
    this.queue = new ArrayDeque<>(Arrays.asList(query.split(delimiters)));
  }

  /**
   * Checks whether preprocessor is not empty.
   *
   * @return {@code false} if no more words left, {@code true} otherwise
   */
  @Override
  public boolean hasNext() {
    while (!queue.isEmpty() && !predicate.test(queue.peek())) {
      queue.poll();
    }

    return !queue.isEmpty();
  }

  /**
   * Performs preprocessing step for the current word.
   *
   * @return preprocessed word
   * @throws NoSuchElementException if no more words left
   */
  @Override
  public String next() {
    if (!hasNext()) {
      throw new NoSuchElementException("Preprocessor has no elements.");
    }

    return mapper.apply(queue.poll());
  }

  /**
   * Builder for {@link QueryPreprocessor}.
   *
   * Features a convenient way to set all the options defined by {@link QueryPreprocessor}.
   *
   * In the case when some of the options are not set this class uses noop equivalent.
   */
  public static final class Builder implements Preprocessor.Builder {
    private String delimiters = DELIMITERS;
    private Function<? super String, String> spellingCorrector = Function.identity();
    private Function<? super String, String> stemmer = Function.identity();
    private Predicate<String> predicate = word -> true;
    private Denoiser denoiser = Denoiser.DUMMY;

    /**
     * Allows to set delimiters {@link String} to split the {@code query} by.
     *
     * @param delimiters to be set
     * @return this instance
     */
    public Builder setDelimiters(@NotNull String delimiters) {
      this.delimiters = delimiters;
      return this;
    }

    /**
     * Allows to set spelling corrector to be used to correct words in the {@code query}.
     *
     * @param spellingCorrector - corrector to be used
     * @return this instance
     */
    public Builder setSpellingCorrector(@NotNull Function<? super String, String> spellingCorrector) {
      this.spellingCorrector = spellingCorrector;
      return this;
    }

    /**
     * Allows to set stemming algorithm to be used by {@link QueryPreprocessor}.
     *
     * @param stemmer - stemming algorithm to be used
     * @return this instance
     */
    public Builder setStemmer(@NotNull Function<? super String, String> stemmer) {
      this.stemmer = stemmer;
      return this;
    }

    /**
     * Allows to set {@link Denoiser} instance to be used for removing noise from the {@code query}.
     *
     * @param denoiser instance to be used by {@link QueryPreprocessor}
     * @return this instance
     */
    public Builder setDenoiser(@NotNull Denoiser denoiser) {
      this.denoiser = denoiser;
      return this;
    }

    /**
     * Allows to add optional filters to filter the words by.
     *
     * @param predicate - filter to be used by {@link QueryPreprocessor}
     * @return this instance
     */
    public Builder addFilter(@NotNull Predicate<? super String> predicate) {
      this.predicate = this.predicate.and(predicate);
      return this;
    }

    /**
     * Builds an instance of {@link QueryPreprocessor} using operations defined.
     *
     * @param query to build an instance for (must not be {@code null})
     * @return instance built
     */
    @NotNull
    @Override
    public Preprocessor build(@NotNull String query) {
      Predicate<String> noise = denoiser::isNoise;
      Predicate<String> filter = noise.negate().and(predicate);
      Function<? super String, String> mapper = stemmer.andThen(spellingCorrector);
      return new QueryPreprocessor(delimiters, filter, mapper, query);
    }
  }
}
