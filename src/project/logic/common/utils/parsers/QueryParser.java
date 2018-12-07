package project.logic.common.utils.parsers;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

@FunctionalInterface
public interface QueryParser {
    @NotNull Stream<String> parse(@NotNull String query);
}
