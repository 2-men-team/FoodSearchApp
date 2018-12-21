package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Stemmer;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Implements data-set-dependent operations for Russian data set.
 * @see LanguageProperties
 */
public final class RussianProperties implements LanguageProperties {
  /**
   * Provides parsing strategy for {@link Location} instance.
   * @param line from the data set file (must not be {@code null})
   * @return parsed {@link Location} instance
   * @see LanguageProperties
   */
  @Override
  public @NotNull Location parseLocation(@NotNull String[] line) {
    return line[5].isEmpty() ? Location.NONE : new Location(line[5]);
  }

  /**
   * Provides parsing strategy for {@link Restaurant} instance.
   * @param line from the data set file (must not be {@code null})
   * @param location to initialize {@link Restaurant} from
   * @return parsed {@link Restaurant} instance
   * @see LanguageProperties
   */
  @Override
  public @NotNull Restaurant parseRestaurant(@NotNull String[] line, Location location) {
    return new Restaurant(line[3], line[4], location);
  }

  /**
   * Provides parsing strategy for {@link Dish} instance.
   * @param line from the data set file (must not be {@code null})
   * @param restaurant to initialize {@link Dish} from
   * @return parsed {@link Dish} instance
   * @see LanguageProperties
   */
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

  /**
   * Provides access to word constraint specified for Russian data set.
   * @return constraint {@link Predicate}
   */
  @Override
  public @NotNull Predicate<String> getWordConstraint() {
    return word -> word.length() > 2;
  }

  /**
   * Provides access to data constraint specified for Russian data set.
   * @return constraint {@link Predicate}
   */
  @Override
  public @NotNull Predicate<Map.Entry<String, Set<Dish>>> getDataConstraint() {
    return entry -> true;
  }

  /**
   * Provides access to stemming algorithm for Russian language.
   * @return {@link Stemmer} instance
   * @see Stemmer
   */
  @Override
  public @NotNull Stemmer getStemmer() {
    return Stemmer.RUSSIAN;
  }
}
