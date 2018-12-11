package project.logic.strategies.handling;

import org.jetbrains.annotations.NotNull;
import project.logic.representation.Dish;
import project.logic.strategies.handling.HandlingStrategy.Result;
import project.logic.strategies.preprocessing.Preprocessor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AnalyticalHandler implements HandlingStrategy<List<Result<Dish>>> {
    private final Preprocessor.Builder builder;
    private final Map<String, Set<Dish>> data;

    public AnalyticalHandler(@NotNull Preprocessor.Builder builder, @NotNull Map<String, Set<Dish>> data) {
        this.builder = builder;
        this.data = data;
    }

    private static final class AnalyticalResult implements Result<Dish> {
        private final Dish data;
        private final double rank;

        private AnalyticalResult(@NotNull Dish dish, double rank) {
            this.data = dish;
            this.rank = rank;
        }

        @Override
        public double getRank() {
            return rank;
        }

        @Override
        public @NotNull Dish getData() {
            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (o.getClass() != this.getClass()) return false;
            AnalyticalResult that = (AnalyticalResult) o;
            return this.data.equals(that.data);
        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }

        @Override
        public String toString() {
            return String.format("AnalyticalResult: %s (rank: %.4f)", data, rank);
        }
    }

    @Override
    public @NotNull List<Result<Dish>> apply(@NotNull String query) {
        return builder.build(query.trim().toLowerCase())
                .asStream()
                .flatMap(entry -> data.get(entry).stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new AnalyticalResult(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
