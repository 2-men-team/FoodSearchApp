package project.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Dish;

import java.io.Serializable;
import java.util.Objects;

public final class Response implements Serializable {
    private final static long serialVersionUID = 686549231346866210L;

    private final String name;
    private final Iterable<Dish> dishes;

    Response(@Nullable String name, @NotNull Iterable<Dish> dishes) {
        this.name = "" + name;
        this.dishes = Objects.requireNonNull(dishes);
    }

    public Iterable<Dish> getDishes() {
        return dishes;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Request: " + name;
    }
}