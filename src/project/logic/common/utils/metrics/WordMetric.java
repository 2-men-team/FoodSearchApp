package project.logic.common.utils.metrics;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@FunctionalInterface
public interface WordMetric<V> extends Serializable {
    V compute(@NotNull String a, @NotNull String b);
}
