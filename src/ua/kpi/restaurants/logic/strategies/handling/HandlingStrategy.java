package ua.kpi.restaurants.logic.strategies.handling;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Restaurant;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface HandlingStrategy extends Function<String, List<HandlingStrategy.Result>> {
  @NotNull List<Result> apply(@NotNull String query);

  interface Result {
    double getRank();

    @NotNull Restaurant getRestaurant();

    @NotNull List<Dish> getDishes();

    static Comparator<Result> comparingByRank() {
      return Comparator.comparingDouble(Result::getRank);
    }

    static Comparator<Result> comparingByRank(@NotNull Comparator<? super Double> comparator) {
      return Comparator.comparing(Result::getRank, comparator);
    }

    static Comparator<Result> comparingByRestaurant(@NotNull Comparator<? super Restaurant> comparator) {
      return Comparator.comparing(Result::getRestaurant, comparator);
    }
  }
}
