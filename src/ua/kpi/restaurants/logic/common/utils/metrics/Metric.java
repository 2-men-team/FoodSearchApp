package ua.kpi.restaurants.logic.common.utils.metrics;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.BiFunction;

@FunctionalInterface
public interface Metric<E, V extends Number> extends Serializable, BiFunction<E, E, V> {
  @Override
  @NotNull V apply(@NotNull E a, @NotNull E b);
}
