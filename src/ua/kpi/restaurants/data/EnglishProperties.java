package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Stemmer;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class EnglishProperties implements LanguageProperties {
  @Override
  public @NotNull Location parseLocation(@NotNull String[] line) {
    return Location.valueOf(line[2], line[3], line[1]);
  }

  @Override
  public @NotNull Restaurant parseRestaurant(@NotNull String[] line, Location location) {
    return new Restaurant(line[0], line[1], location);
  }

  @Override
  public @NotNull Dish parseDish(@NotNull String[] line, Restaurant restaurant) {
    double price;
    try {
      price = Double.parseDouble(line[5]);
    } catch (NumberFormatException e) {
      price = Double.NaN;
    }

    return new Dish(line[4], restaurant, price);
  }

  @Override
  public @NotNull Predicate<String> getWordConstraint() {
    Predicate<String> predicate = word -> word.length() > 2;
    return predicate.and(Pattern.compile("[a-zA-Z]+").asPredicate());
  }

  @Override
  public @NotNull Predicate<Map.Entry<String, Set<Dish>>> getDataConstraint() {
    return entry -> entry.getValue().size() >= 150;
  }

  @Override
  public @NotNull Stemmer getStemmer() {
    return Stemmer.ENGLISH;
  }
}
