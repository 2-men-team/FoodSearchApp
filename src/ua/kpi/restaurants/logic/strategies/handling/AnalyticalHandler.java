package ua.kpi.restaurants.logic.strategies.handling;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.preprocessing.Preprocessor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AnalyticalHandler implements HandlingStrategy {
  private final Preprocessor.Builder builder;
  private final Map<String, Set<Dish>> data;

  public AnalyticalHandler(@NotNull Preprocessor.Builder builder, @NotNull Map<String, Set<Dish>> data) {
    this.builder = builder;
    this.data = data;
  }

  public static final class AnalyticalResult implements Result {
    private final Restaurant restaurant;
    private final List<Dish> dishes;
    private double rank;

    private AnalyticalResult(@NotNull Restaurant restaurant, @NotNull List<Dish> dishes, double rank) {
      this.restaurant = restaurant;
      this.dishes = dishes;
      this.rank = rank;
    }

    @Override
    public double getRank() {
      return rank;
    }

    @Override
    public @NotNull List<Dish> getDishes() {
      return dishes;
    }

    @Override
    public @NotNull Restaurant getRestaurant() {
      return restaurant;
    }

    @Override
    public boolean equals(Object o) {
      if (o == null) return false;
      if (o == this) return true;
      if (o.getClass() != this.getClass()) return false;
      AnalyticalResult that = (AnalyticalResult) o;
      return this.restaurant.equals(that.restaurant);
    }

    @Override
    public int hashCode() {
      return restaurant.hashCode();
    }

    @Override
    public String toString() {
      return String.format("AnalyticalResult: %s (rank: %.4f)", restaurant, rank);
    }
  }

  @Override
  public @NotNull List<Result> apply(@NotNull String query) {
    Map<Dish, Long> map = builder.build(query.trim().toLowerCase()).asStream()
        .flatMap(entry -> data.get(entry).stream())
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    Map<Restaurant, AnalyticalResult> resultMap = new HashMap<>();
    for (Map.Entry<Dish, Long> entry : map.entrySet()) {
      Dish dish = entry.getKey();
      Restaurant restaurant = dish.getRestaurant();
      double rank = entry.getValue();

      AnalyticalResult result = resultMap
          .computeIfAbsent(restaurant, key -> new AnalyticalResult(key, new ArrayList<>(), 0.0));
      result.dishes.add(dish);
      result.rank += rank;
    }

    resultMap.values().forEach(result -> {
      result.rank /= result.dishes.size();
      result.dishes.sort(Dish.comparingByPrice().reversed());
    });

    return new ArrayList<>(resultMap.values());
  }
}
