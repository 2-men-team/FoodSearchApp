package project.logic.handlers;

import org.jetbrains.annotations.NotNull;
import project.logic.representation.Dish;
import project.logic.representation.Location;
import project.logic.representation.Restaurant;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface QueryHandler {
    @NotNull Iterable<Result> handle(@NotNull String query);

    interface Result {
        double getRank();
        @NotNull Dish getDish();

        @NotNull
        static Comparator<Result> comparingByRank() {
            return Comparator.comparingDouble(Result::getRank);
        }

        @NotNull
        static Comparator<Result> comparingByLocation(@NotNull Location location) {
            Objects.requireNonNull(location);
            Function<Result, Dish> a = Result::getDish;
            Function<Result, Restaurant> b = a.andThen(Dish::getRestaurant);
            return Comparator.comparing(b.andThen(Restaurant::getLocation), location.getComparator());
        }
    }

    enum MethodType { MACHINE_LEARNING, ANALYTICAL }

    class Factory {
        private static final Map<MethodType, Class<? extends QueryHandler>> CLASS_MAP = new HashMap<>();

        static {
            CLASS_MAP.put(MethodType.ANALYTICAL, AnalyticalHandler.class);
        }

        public static QueryHandler getByMethodType(@NotNull MethodType method) throws IllegalAccessException, InstantiationException {
            return CLASS_MAP.get(method).newInstance();
        }
    }
}
