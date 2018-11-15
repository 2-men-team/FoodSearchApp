package project.logic.handlers;

import org.jetbrains.annotations.NotNull;
import project.logic.DataBase;
import project.logic.common.utils.Denoiser;
import project.logic.representation.Dish;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AnalyticalHandler implements QueryHandler {
    private final Denoiser denoiser = new Denoiser(DataBase.getInstance().getStopWords());

    public static final class AnalyticalResult implements Result {
        private final Dish   dish;
        private final double rank;

        private AnalyticalResult(Dish dish, double rank) {
            this.dish = dish;
            this.rank = rank;
        }

        @Override
        public double getRank() {
            return rank;
        }

        @Override
        public @NotNull Dish getDish() {
            return dish;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (o.getClass() != this.getClass()) return false;
            AnalyticalResult that = (AnalyticalResult) o;
            return this.dish.equals(that.dish);
        }

        @Override
        public int hashCode() {
            return dish.hashCode();
        }

        @Override
        public String toString() {
            return String.format("AnalyticalResult: %s (rank: %.4f)", dish, rank);
        }
    }

    @Override
    public @NotNull Iterable<Result> handle(@NotNull String query) {
        Map<String, Set<Dish>> data = DataBase.getInstance().getData();

        return denoiser.clear(query)
                .stream()
                .flatMap(DataBase.getInstance().getSimilarities()::stream)
                .flatMap(entry -> data.get(entry.getElement()).stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new AnalyticalResult(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
