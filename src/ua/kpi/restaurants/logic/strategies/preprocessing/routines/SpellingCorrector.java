package ua.kpi.restaurants.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;

import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * Preprocessing routine that performs spell checking and correction.
 *
 * It uses {@link Set} of correct words and {@link SimilaritySet} to correct misspellings.
 * It checks whether given word is in the first set. If it is not, it tries to correct it.
 * If the word is a noise, it ignores it.
 *
 */
public final class SpellingCorrector implements UnaryOperator<String> {
  private final Set<String> correctWords;
  private final Denoiser denoiser;
  private final SimilaritySet<String> similarities;

  /**
   * Constructs routine from the given information
   *
   * @param correctWords - {@link Set} of correct words
   * @param denoiser - {@link Denoiser} instance
   * @param similarities - {@link SimilaritySet} of correct words
   */
  public SpellingCorrector(
      @NotNull Set<String> correctWords,
      @NotNull Denoiser denoiser,
      @NotNull SimilaritySet<String> similarities
  ) {
    this.correctWords = correctWords;
    this.denoiser = denoiser;
    this.similarities = similarities;
  }

  /**
   * Performs computation for the given word.
   *
   * It performs the following operations depending on the words status:
   * <ul>
   *   <li>if word is a noise or a correct one, algorithm ignores it</li>
   *   <li>if word is not a noise and is incorrect, algorithm returns the most similar word to the current one</li>
   * </ul>
   *
   * If there are no similar in the last case, algorithm returns empty {@link String}.
   * This behaviour is chosen instead of returning {@code null} because it allows subsequent algorithms to
   * perform string manipulations without a fear to get {@link NullPointerException}.
   *
   * @param word to be processed (must not be {@code null})
   * @return processed word
   */
  @Override
  public @NotNull String apply(@NotNull String word) {
    if (!correctWords.contains(word) && !denoiser.isNoise(word)) {
      word = similarities.getSimilarTo(word).stream()
          .min(SimilaritySet.Entry.comparingBySimilarity())
          .map(SimilaritySet.Entry::getElement).orElse("");
    }

    return word;
  }
}
