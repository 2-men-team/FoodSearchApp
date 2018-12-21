package ua.kpi.restaurants.logic.common.utils.metrics;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * Describes a metric to be used with words
 *
 * This metric can be used to retrieve different properties of words (e.g. their similarity)
 *
 * @param <E> type of words
 * @param <V> type of property to retrieve
 */
@FunctionalInterface
public interface Metric<E, V extends Number> extends Serializable, BiFunction<E, E, V> {
  /**
   * Performs property retrieval
   *
   * @param a first word (must not be {@code null})
   * @param b second word (must not be {@code null})
   * @return retrieved property
   */
  @Override
  @NotNull V apply(@NotNull E a, @NotNull E b);
}
