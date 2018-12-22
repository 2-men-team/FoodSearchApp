package ua.kpi.restaurants.network.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy.Result;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Defines major ordering techniques.
 *
 * This implementation aimed at efficient technique selection and ability to easily serialize it using JSON.
 * Enum was chosen because it satisfies both criteria:
 * <ol>
 *   <li>it can be easily serialized and deserialized</li>
 *   <li>it provides convenient way to select particular ordering without any additional conditioning cases</li>
 * </ol>
 *
 * Every enum value has a function to map from particular {@link Request} to the {@link Comparator}
 * for the corresponding order.
 *
 * Ordering is done on results of the {@link ua.kpi.restaurants.logic.strategies.handling.AnalyticalHandler}.
 */
public enum Ordering implements Function<Request, Comparator<Result<Dish>>> {
  /** Provides ordering by rank */
  BY_RANK(request -> Result.comparingByRank()),

  /** Provides ordering by price */
  BY_PRICE(request -> Result.comparingByData(Dish.comparingByPrice())),

  /** Provides ordering by {@link ua.kpi.restaurants.logic.representation.Location} */
  BY_LOCATION(request -> {
    Comparator<Restaurant> restaurantComparator = Restaurant.comparingByLocation(request.getLocation());
    Comparator<Dish> dishComparator = Comparator.comparing(Dish::getRestaurant, restaurantComparator);
    return Result.comparingByData(dishComparator);
  });

  private final Function<Request, Comparator<Result<Dish>>> mapper;

  Ordering(Function<Request, Comparator<Result<Dish>>> mapper) {
    this.mapper = mapper;
  }

  /**
   * Retrieves order {@link Comparator} based on the {@link Request} data.
   *
   * @param request to order data for (must not be {@code null})
   * @return {@link Comparator} instance for the chosen ordering
   */
  @Override
  public Comparator<Result<Dish>> apply(@NotNull Request request) {
    return mapper.apply(request);
  }

  /**
   * Specifies additional rule to be applied to {@link Ordering} {@link Comparator}.
   */
  public enum Rule implements UnaryOperator<Comparator<Result<Dish>>> {
    /** Does nothing for given {@link Comparator} */
    NORMAL(UnaryOperator.identity()),

    /** Reverses the order of comparison */
    REVERSED(Comparator::reversed);

    private final UnaryOperator<Comparator<Result<Dish>>> operator;

    Rule(UnaryOperator<Comparator<Result<Dish>>> operator) {
      this.operator = operator;
    }

    /**
     * Applies chosen rule to the given {@link Comparator}.
     *
     * @param comparator for rule to be applied to (must not be {@code null})
     * @return modified {@link Comparator} with rule applied
     */
    @Override
    public Comparator<Result<Dish>> apply(@NotNull Comparator<Result<Dish>> comparator) {
      return operator.apply(comparator);
    }
  }
}
