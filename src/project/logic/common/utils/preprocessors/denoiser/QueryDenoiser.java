package project.logic.common.utils.preprocessors.denoiser;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class QueryDenoiser implements Denoiser {
    private final Set<String> stopWords;

    public QueryDenoiser(@NotNull Set<String> stopWords) {
        this.stopWords = stopWords;
    }

    @Override
    public boolean isNoise(@NotNull String word) {
        return stopWords.contains(word);
    }

    @Override
    @NotNull
    public Stream<String> asStream(@NotNull List<String> query) {
        return query.stream().filter(word -> !isNoise(word));
    }

    @Override
    @NotNull
    public Iterable<String> clear(@NotNull List<String> query) {
        return asStream(query).collect(Collectors.toList());
    }
}
