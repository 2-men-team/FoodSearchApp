package ua.kpi.restaurants.logic.strategies.handling;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * The basic interface that describes strategy of solving the given problem.
 *
 * The main design goal was to develop a flexible interface capable of describing different strategies
 * of solving this particular task. The reason to do that was an immense amount of different algorithms and methods of
 * addressing the problem and their different implementations. The ability to easily extend the implementation and
 * add new algorithms was considered the most important.
 *
 * This interface describes basic functionality that every implementation of solving technique must follow.
 *
 * Interface extends {@link Function} interface to provide convenient mapping from given query described as {@link String}
 * to results of algorithm evaluation.
 *
 * @param <R> describes type of the value returned
 *           (typically {@link ua.kpi.restaurants.logic.representation.Restaurant} or {@link ua.kpi.restaurants.logic.representation.Dish})
 */
@FunctionalInterface
public interface HandlingStrategy<R> extends Function<String, List<HandlingStrategy.Result<R>>> {
  /**
   * Applies this strategy to solve a problem.
   *
   * @param query to be preprocessed (must not be {@code null})
   * @return results of algorithm evaluation
   */
  @NotNull List<Result<R>> apply(@NotNull String query);

  /**
   * Specification of the interface capable of holding both {@code data} and its {@code rank}.
   * Rank is computed according to the algorithm specification. It shows how well current {@code data} fits
   * to given query.
   *
   * @param <E> type of data
   */
  interface Result<E> {
    /**
     * Retrieves the rank of the current data element.
     *
     * @return rank retrieved
     */
    double getRank();

    /**
     * Retrieves the data under consideration.
     *
     * @return data retrieved
     */
    @NotNull E getData();

    /**
     * Produces {@link Comparator} to compare instances of this interface by the rank of their data.
     * This is one of two overloaded methods ({@link #comparingByRank(Comparator)} is the other one).
     *
     * @param <E> - type of data
     * @return produced {@link Comparator}
     */
    static <E> Comparator<Result<E>> comparingByRank() {
      return Comparator.comparingDouble(Result::getRank);
    }

    /**
     * Produces {@link Comparator} to compare instances of this interface by the rank of their data.
     * It allows additional comparator to be included.
     * This is one of two overloaded methods ({@link #comparingByRank()} is the other one).
     *
     * @param comparator - additional comparator
     * @param <E> - type of data
     * @return produced {@link Comparator}
     */
    static <E> Comparator<Result<E>> comparingByRank(@NotNull Comparator<? super Double> comparator) {
      return Comparator.comparing(Result::getRank, comparator);
    }

    /**
     * Produces {@link Comparator} to compare instances of this interface by the data itself.
     * This is one of two overloaded methods ({@link #comparingByData(Comparator)} is the other one).
     *
     * @param <E> - type of data (must be {@link Comparable})
     * @return produced {@link Comparator}
     */
    static <E extends Comparable<? super E>> Comparator<Result<E>> comparingByData() {
      return Comparator.comparing(Result::getData);
    }

    /**
     * Produces {@link Comparator} to compare instances of this interface by the data itself.
     * It needs additional comparator to be included.
     * This is one of two overloaded methods ({@link #comparingByData()} is the other one).
     *
     * @param comparator - additional comparator
     * @param <E> type of data (not necessarily {@link Comparable})
     * @return produced {@link Comparator}
     */
    static <E> Comparator<Result<E>> comparingByData(@NotNull Comparator<? super E> comparator) {
      return Comparator.comparing(Result::getData, comparator);
    }
  }
}
