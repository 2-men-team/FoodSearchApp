package project.logic.handlers;

import org.jetbrains.annotations.NotNull;
import project.logic.representation.Dish;
import project.logic.representation.Location;
import project.logic.representation.Restaurant;

import java.util.Comparator;
import java.util.function.Function;

public interface QueryHandler {
    @NotNull Iterable<Result> handle(@NotNull String query);

    interface Result {
        double getRank();
        @NotNull Dish getDish();

        static Comparator<Result> comparingByRank() {
            return Comparator.comparingDouble(Result::getRank);
        }

        static Comparator<Result> comparingByLocation(@NotNull Location location) {
            Function<Result, Dish> a = Result::getDish;
            Function<Result, Restaurant> b = a.andThen(Dish::getRestaurant);
            return Comparator.comparing(b.andThen(Restaurant::getLocation), location.getComparator());
        }
    }
}
