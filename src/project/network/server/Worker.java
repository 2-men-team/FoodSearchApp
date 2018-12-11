package project.network.server;

import org.jetbrains.annotations.NotNull;
import project.logic.common.exceptions.ProjectRuntimeException;
import project.logic.common.utils.Serializer;
import project.logic.representation.Restaurant;
import project.logic.strategies.handling.HandlingStrategy;
import project.logic.representation.Dish;
import project.logic.strategies.handling.HandlingStrategy.Result;
import project.network.data.Request;
import project.network.data.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

final class Worker implements Runnable {
    private final Socket socket;
    private final HandlingStrategy<List<Result<Dish>>> handler;

    Worker(@NotNull Socket socket, @NotNull HandlingStrategy<List<Result<Dish>>> handler) {
        this.socket  = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        try (Socket s = socket) {
            Request request = Serializer.deserializeJson(s.getInputStream(), Request.class);
            System.out.println(request);

            Response response;
            try {
                List<Dish> dishes = handler.apply(request.getQuery())
                        .stream()
                        .sorted(Result.<Dish>comparingByRank().reversed())
                        .limit(20)
                        .map(Result::getData)
                        .collect(Collectors.toList());

                List<Dish> byRank = dishes.stream().limit(10).collect(Collectors.toList());
                List<Dish> byLocation = dishes
                        .stream()
                        .skip(10)
                        .sorted(Comparator.comparing(Dish::getRestaurant, Restaurant.comparingByLocation(request.getLocation())))
                        .collect(Collectors.toList());

                response = Response.success(request.getMessage(), byLocation, byRank);
            } catch (ProjectRuntimeException e) {
                String template = "Failed while processing data for %s: %s";
                response = Response.failure(String.format(template, request.getQuery(), e.getMessage()));
            }

            Serializer.serializeJson(s.getOutputStream(), response);
        } catch (IOException e) {
            throw new RuntimeException("Error while processing data", e);
        }
    }
}
