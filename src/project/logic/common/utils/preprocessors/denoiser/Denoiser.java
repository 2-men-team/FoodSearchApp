package project.logic.common.utils.preprocessors.denoiser;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public interface Denoiser {
    Denoiser DUMMY = new Denoiser() {
        @Override
        public boolean isNoise(@NotNull String word) {
            return false;
        }

        @NotNull
        @Override
        public Stream<String> asStream(@NotNull List<String> query) {
            return query.stream();
        }

        @NotNull
        @Override
        public Iterable<String> clear(@NotNull List<String> query) {
            return query;
        }
    };

    boolean isNoise(@NotNull String word);
    @NotNull Stream<String> asStream(@NotNull List<String> query);
    @NotNull Iterable<String> clear(@NotNull List<String> query);

    default Stream<String> asStream(@NotNull String[] query) {
        return asStream(Arrays.asList(query));
    }

    default Iterable<String> clear(@NotNull String[] query) {
        return clear(Arrays.asList(query));
    }
}
