package project.logic.common.utils.preprocessors.mappers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@FunctionalInterface
public interface Mapper {
    Mapper DUMMY = word -> word;

    @NotNull String mapWord(@NotNull String word);

    default Iterable<String> mapQuery(@NotNull Iterable<String> query) {
        List<String> list = new ArrayList<>();

        for (String word : query) {
            list.add(mapWord(Objects.requireNonNull(word)));
        }

        return list;
    }
}
