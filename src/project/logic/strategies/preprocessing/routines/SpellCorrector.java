package project.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;
import project.logic.common.algorithms.SimilaritySet;
import project.logic.common.exceptions.InvalidQueryException;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class SpellCorrector implements Function<String, String> {
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
