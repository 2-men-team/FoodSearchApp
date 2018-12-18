package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Stemmer;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final class RussianProperties implements LanguageProperties {
  @Override
  public @NotNull Location parseLocation(@NotNull String[] line) {
    return line[5].isEmpty() ? Location.NONE : new Location(line[5]);
  }

  @Override
  public @NotNull Restaurant parseRestaurant(@NotNull String[] line, Location location) {
    return new Restaurant(line[3], line[4], location);
  }

  @Override
  public @NotNull Dish parseDish(@NotNull String[] line, Restaurant restaurant) {
    double price;
    try {
      price = Double.parseDouble(line[2]);
    } catch (NumberFormatException e) {
      price = Double.NaN;
    }

    return new Dish(line[1], restaurant, price);
  }

  @Override
  public @NotNull Predicate<String> getWordConstraint() {
    return word -> word.length() > 2;
  }

  @Override
  public @NotNull Predicate<Map.Entry<String, Set<Dish>>> getDataConstraint() {
    return entry -> true;
  }

  @Override
  public @NotNull Stemmer getStemmer() {
    return Stemmer.RUSSIAN;
  }
}
