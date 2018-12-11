package project.logic.strategies.preprocessing;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import project.logic.strategies.preprocessing.routines.Denoiser;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Function;

public final class QueryPreprocessor implements Preprocessor {
    @Language("RegExp")
    public static final String DELIMITERS = "(\\s|\\d|\\p{Punct})+";

    private final Denoiser denoiser;
    private final Function<String, String> mapper;
    private final Queue<String> queue;

    private QueryPreprocessor(String delimiters, Denoiser denoiser, Function<String, String> mapper, String query) {
        this.denoiser = denoiser;
        this.mapper = mapper;
        this.queue = new ArrayDeque<>(Arrays.asList(query.split(delimiters)));
    }

    @Override
    public boolean hasNext() {
        while (!queue.isEmpty() && denoiser.isNoise(queue.peek())) {
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

        @NotNull
        @Override
        public Preprocessor build(@NotNull String query) {
            return new QueryPreprocessor(delimiters, denoiser, stemmer.andThen(spellCorrector), query);
        }
    }
}
