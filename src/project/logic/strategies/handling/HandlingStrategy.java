package project.logic.strategies.handling;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

@FunctionalInterface
public interface HandlingStrategy<E> extends Function<String, E> {
    @NotNull E apply(@NotNull String query);

    interface Result<R> {
        double getRank();
        @NotNull R getData();

        static <R> Comparator<Result<R>> comparingByRank() {
            return Comparator.comparingDouble(Result::getRank);
        }

        static <R> Comparator<Result<R>> comparingByRank(@NotNull Comparator<? super Double> comparator) {
            return Comparator.comparing(Result::getRank, comparator);
        }

        static <R extends Comparable<R>> Comparator<Result<R>> comparingByData() {
            return Comparator.comparing(Result::getData);
        }

        static <R> Comparator<Result<R>> comparingByData(@NotNull Comparator<? super R> comparator) {
            return Comparator.comparing(Result::getData, comparator);
        }
    }
}
