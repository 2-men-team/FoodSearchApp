package project.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Dish;

import java.io.Serializable;

public final class Response implements Serializable {
    private final static long serialVersionUID = -7764099569087263618L;

    private final String name;
    private final Iterable<Dish> dishes;

    public Response(@Nullable String name, @NotNull Iterable<Dish> dishes) {
        this.name = "" + name;
        this.dishes = dishes;
    }

    @NotNull
    public Iterable<Dish> getDishes() {
        return dishes;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Request: " + name;
    }
}