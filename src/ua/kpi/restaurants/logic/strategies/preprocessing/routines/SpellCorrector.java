package ua.kpi.restaurants.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.common.exceptions.InvalidQueryException;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

public final class SpellCorrector implements UnaryOperator<String> {
  private final Set<String> correctWords;
  private final Denoiser denoiser;
  private final SimilaritySet<String> similarities;

  public SpellCorrector(
      @NotNull Set<String> correctWords,
      @NotNull Denoiser denoiser,
      @NotNull SimilaritySet<String> similarities
  ) {
    this.correctWords = correctWords;
    this.denoiser = denoiser;
    this.similarities = similarities;
  }

  @Override
  public @NotNull String apply(@NotNull String word) {
    if (!correctWords.contains(word) && !denoiser.isNoise(word)) {
      Comparator<SimilaritySet.Entry<String>> comparator = SimilaritySet.Entry.comparingBySimilarity();
      Optional<SimilaritySet.Entry<String>> optional = similarities.stream(word).min(comparator);

      if (!optional.isPresent()) {
        throw new InvalidQueryException("Unknown word passed: " + word);
      }

      word = optional.get().getElement();
    }

    return word;
  }
}
