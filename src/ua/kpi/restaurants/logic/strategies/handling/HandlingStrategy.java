package ua.kpi.restaurants.logic.strategies.handling;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface HandlingStrategy<R> extends Function<String, List<HandlingStrategy.Result<R>>> {
  @NotNull List<Result<R>> apply(@NotNull String query);

  interface Result<E> {
    double getRank();

    @NotNull E getData();

    static <E> Comparator<Result<E>> comparingByRank() {
      return Comparator.comparingDouble(Result::getRank);
    }

    static <E> Comparator<Result<E>> comparingByRank(@NotNull Comparator<? super Double> comparator) {
      return Comparator.comparing(Result::getRank, comparator);
    }

    static <E extends Comparable<? super E>> Comparator<Result<E>> comparingByData() {
      return Comparator.comparing(Result::getData);
    }

    static <E> Comparator<Result<E>> comparingByData(@NotNull Comparator<? super E> comparator) {
      return Comparator.comparing(Result::getData, comparator);
    }
  }
}
