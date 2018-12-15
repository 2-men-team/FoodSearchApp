package ua.kpi.restaurants.logic.strategies.handling;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.strategies.preprocessing.Preprocessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AnalyticalHandler implements HandlingStrategy<Dish> {
  private final Preprocessor.Builder builder;
  private final Map<String, Set<Dish>> data;

  public AnalyticalHandler(@NotNull Preprocessor.Builder builder, @NotNull Map<String, Set<Dish>> data) {
    this.builder = builder;
    this.data = data;
  }

  public static final class AnalyticalResult implements Result<Dish> {
    private final Dish data;
    private double rank;

    private AnalyticalResult(@NotNull Dish data, double rank) {
      this.data = data;
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
    Map<Dish, AnalyticalResult> map = new HashMap<>();

    for (String word : builder.build(query.trim().toLowerCase())) {
      for (Dish dish : data.getOrDefault(word, Collections.emptySet())) {
        AnalyticalResult result = map
            .computeIfAbsent(dish, key -> new AnalyticalResult(key, 0.0));
        result.rank++;
      }
    }

    return new ArrayList<>(map.values());
  }
}
