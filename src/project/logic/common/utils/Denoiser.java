package project.logic.common.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Denoiser {
    private final Set<String> stopWords;

    public Denoiser(@NotNull Set<String> stopWords) {
        this.stopWords = stopWords;
    }

    public List<String> clear(@NotNull String query, @NotNull String separators) {
        return Pattern.compile(Objects.requireNonNull(separators))
                .splitAsStream(Objects.requireNonNull(query))
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toList());
    }

    public List<String> clear(@NotNull String query) {
        return clear(query, "(\\s|\\d|\\p{Punct})+");
    }
}
