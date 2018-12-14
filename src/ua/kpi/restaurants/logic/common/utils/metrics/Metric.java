package ua.kpi.restaurants.logic.common.utils.metrics;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@FunctionalInterface
public interface Metric<E, V extends Number> extends Serializable {
  V compute(@NotNull E a, @NotNull E b);
}
