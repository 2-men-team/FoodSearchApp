package project.network.response;

import org.jetbrains.annotations.NotNull;
import project.logic.representation.Dish;

import java.io.Serializable;
import java.util.List;

public interface Response extends Serializable {
    @NotNull List<Dish> getData();
    @NotNull String getMessage();
    @NotNull Status getStatus();

    enum Status {
        BAD,
        GOOD
    }
}