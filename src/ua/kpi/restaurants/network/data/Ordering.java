package ua.kpi.restaurants.network.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy.Result;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public enum Ordering implements Function<Request, Comparator<Result<Dish>>> {
  BY_RANK(request -> Result.comparingByRank()),
  BY_PRICE(request -> Result.comparingByData(Dish.comparingByPrice())),
  BY_LOCATION(request -> {
    Comparator<Restaurant> restaurantComparator = Restaurant.comparingByLocation(request.getLocation());
    Comparator<Dish> dishComparator = Comparator.comparing(Dish::getRestaurant, restaurantComparator);
    return Result.comparingByData(dishComparator);
  });

  private final Function<Request, Comparator<Result<Dish>>> mapper;

  Ordering(Function<Request, Comparator<Result<Dish>>> mapper) {
    this.mapper = mapper;
  }

  @Override
  public Comparator<Result<Dish>> apply(@NotNull Request request) {
    return mapper.apply(request);
  }

  public enum Rule implements UnaryOperator<Comparator<Result<Dish>>> {
    NORMAL(UnaryOperator.identity()),
    REVERSED(Comparator::reversed);

    private final UnaryOperator<Comparator<Result<Dish>>> operator;

    Rule(UnaryOperator<Comparator<Result<Dish>>> operator) {
      this.operator = operator;
    }

    @Override
    public Comparator<Result<Dish>> apply(@NotNull Comparator<Result<Dish>> comparator) {
      return operator.apply(comparator);
    }
  }
}
