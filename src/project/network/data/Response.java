package project.network.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.representation.Dish;

import java.util.List;

public final class Response {
    private final String message;
    private final List<Dish> dataByLocation;
    private final List<Dish> dataByRank;
    private final Status status;

    // TODO: remove redundant 'data' fields
    private Response(String message, List<Dish> dataByLocation, List<Dish> dataByRank, Status status) {
        this.message = message;
        this.dataByLocation = dataByLocation;
        this.dataByRank = dataByRank;
        this.status = status;
    }

    public static Response success(@Nullable String message, @NotNull List<Dish> dataByLocation, @NotNull List<Dish> dataByRank) {
        return new Response(message, dataByLocation, dataByRank, Status.SUCCESS);
    }

    public static Response failure(@Nullable String message) {
        return new Response(message, null, null, Status.FAILURE);
    }

    public @NotNull List<Dish> getDataByLocation() {
        if (status == Status.FAILURE) {
            throw new UnsupportedOperationException("Failed response has no data.");
        }

        return dataByLocation;
    }

    public @NotNull List<Dish> getDataByRank() {
        if (status == Status.FAILURE) {
            throw new UnsupportedOperationException("Failed response has no data.");
        }

        return dataByRank;
    }

    public @Nullable String getMessage() {
        return message;
    }

    public @NotNull Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("Response: { %s, %s }", status, message);
    }

    public enum Status {
        FAILURE,
        SUCCESS
    }
}