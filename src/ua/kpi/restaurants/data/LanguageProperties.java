package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Stemmer;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public interface LanguageProperties {
  @NotNull Location parseLocation(@NotNull String[] line);

  @NotNull Restaurant parseRestaurant(@NotNull String[] line, Location location);

  @NotNull Dish parseDish(@NotNull String[] line, Restaurant restaurant);

  @NotNull Predicate<String> getWordConstraint();

  @NotNull Predicate<Map.Entry<String, Set<Dish>>> getDataConstraint();

  @NotNull Stemmer getStemmer();
}
