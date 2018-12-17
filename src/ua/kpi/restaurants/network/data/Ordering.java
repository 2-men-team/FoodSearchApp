package ua.kpi.restaurants.network.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.representation.Restaurant;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy.Result;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public enum Ordering implements Function<Request, Comparator<Result<Dish>>> {
  BY_PRICE {
    @Override public Comparator<Result<Dish>> apply(@NotNull Request request) {
      return Result.comparingByData(Dish.comparingByPrice());
    }
  },

  BY_LOCATION {
    @Override public Comparator<Result<Dish>> apply(@NotNull Request request) {
      Comparator<Restaurant> restaurantComparator = Restaurant.comparingByLocation(request.getLocation());
      Comparator<Dish> dishComparator = Comparator.comparing(Dish::getRestaurant, restaurantComparator);
      return Result.comparingByData(dishComparator);
    }
  },

  BY_RANK {
    @Override public Comparator<Result<Dish>> apply(@NotNull Request request) {
      return Result.comparingByRank();
    }
  };

  public enum Rule implements UnaryOperator<Comparator<Result<Dish>>> {
    NORMAL {
      @Override public Comparator<Result<Dish>> apply(@NotNull Comparator<Result<Dish>> comparator) {
        return comparator;
      }
    },

    REVERSED {
      @Override public Comparator<Result<Dish>> apply(@NotNull Comparator<Result<Dish>> comparator) {
        return comparator.reversed();
      }
    }
  }
}
