package ua.kpi.restaurants.logic.strategies.preprocessing;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Denoiser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Predicate;

public final class QueryPreprocessor implements Preprocessor {
  @Language("RegExp")
  public static final String DELIMITERS = "(\\s|\\p{Punct}|[«»№])+";

  private final Predicate<String> predicate;
  private final Function<String, String> mapper;
  private final Queue<String> queue;

  private QueryPreprocessor(
      String delimiters,
      Predicate<String> predicate,
      Function<String, String> mapper,
      String query
  ) {
    this.predicate = predicate;
    this.mapper = mapper;
    this.queue = new ArrayDeque<>(Arrays.asList(query.split(delimiters)));
  }

  @Override
  public boolean hasNext() {
    while (!queue.isEmpty() && !predicate.test(queue.peek())) {
      queue.poll();
    }

    return !queue.isEmpty();
  }

  @Override
  public String next() {
    if (!hasNext()) {
      throw new NoSuchElementException("Preprocessor has no elements.");
    }

    return mapper.apply(queue.poll());
  }

  public static final class Builder implements Preprocessor.Builder {
    private String delimiters = DELIMITERS;
    private Function<String, String> spellCorrector = Function.identity();
    private Function<String, String> stemmer = Function.identity();
    private List<Predicate<String>> predicates = new ArrayList<>();
    private Denoiser denoiser = Denoiser.DUMMY;

    public Builder setDelimiters(@NotNull String delimiters) {
      this.delimiters = delimiters;
      return this;
    }

    public Builder setSpellCorrector(@NotNull Function<String, String> spellCorrector) {
      this.spellCorrector = spellCorrector;
      return this;
    }

    public Builder setStemmer(@NotNull Function<String, String> stemmer) {
      this.stemmer = stemmer;
      return this;
    }

    public Builder setDenoiser(@NotNull Denoiser denoiser) {
      this.denoiser = denoiser;
      return this;
    }

    public Builder addFilter(@NotNull Predicate<String> predicate) {
      this.predicates.add(predicate);
      return this;
    }

    @NotNull
    @Override
    public Preprocessor build(@NotNull String query) {
      Predicate<String> noise = denoiser::isNoise;
      Predicate<String> predicate = predicates.stream().reduce(noise.negate(), Predicate::and);
      return new QueryPreprocessor(delimiters, predicate, stemmer.andThen(spellCorrector), query);
    }
  }
}
