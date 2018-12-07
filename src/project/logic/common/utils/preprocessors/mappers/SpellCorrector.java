package project.logic.common.utils.preprocessors.mappers;

import org.jetbrains.annotations.NotNull;
import project.logic.common.algorithms.SimilaritySet;
import project.logic.common.exceptions.InvalidQueryException;
import project.logic.common.utils.preprocessors.denoiser.Denoiser;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public final class SpellCorrector implements Mapper {
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
    public @NotNull String mapWord(@NotNull String word) {
        if (!correctWords.contains(word) || !denoiser.isNoise(word)) {
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
