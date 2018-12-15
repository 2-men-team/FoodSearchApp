package ua.kpi.restaurants.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;

import java.util.Set;
import java.util.function.UnaryOperator;

public final class SpellingCorrector implements UnaryOperator<String> {
  private final Set<String> correctWords;
  private final Denoiser denoiser;
  private final SimilaritySet<String> similarities;

  public SpellingCorrector(
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
      word = similarities.stream(word)
          .min(SimilaritySet.Entry.comparingBySimilarity())
          .map(SimilaritySet.Entry::getElement).orElse("");
    }

    return word;
  }
}
