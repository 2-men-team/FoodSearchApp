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
 * Specifies interface to unify data-set-dependent operations.
 * It is mainly used during data base initialization.
 * {@link Config} class uses instances of this interface to provide language properties
 * based on its initialization parameters.
 *
 * @see DataBase
 * @see Config
 */
public interface LanguageProperties {
  /**
   * Provides parsing strategy for {@link Location} instance.
   * @param line from the data set file (must not be {@code null})
   * @return parsed {@link Location} instance
   * @see Location
   */
  @NotNull Location parseLocation(@NotNull String[] line);

  /**
   * Provides parsing strategy for {@link Restaurant} instance.
   * @param line from the data set file (must not be {@code null})
   * @param location to initialize {@link Restaurant} from
   * @return parsed {@link Restaurant} instance
   * @see Restaurant
   */
  @NotNull Restaurant parseRestaurant(@NotNull String[] line, Location location);

  /**
   * Provides parsing strategy for {@link Dish} instance.
   * @param line from the data set file (must not be {@code null})
   * @param restaurant to initialize {@link Dish} from
   * @return parsed {@link Dish} instance
   * @see Dish
   */
  @NotNull Dish parseDish(@NotNull String[] line, Restaurant restaurant);

  /**
   * Provides access to word constraint specified for current data set.
   * @return constraint {@link Predicate}
   */
  @NotNull Predicate<String> getWordConstraint();

  /**
   * Provides access to data constraint specified for current data set.
   * @return constraint {@link Predicate}
   */
  @NotNull Predicate<Map.Entry<String, Set<Dish>>> getDataConstraint();

  /**
   * Provides access to stemming algorithm for current language.
   * @return {@link Stemmer} instance
   * @see Stemmer
   */
  @NotNull Stemmer getStemmer();
}
