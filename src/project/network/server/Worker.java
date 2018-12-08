package project.network.server;

import org.jetbrains.annotations.NotNull;
import project.logic.common.exceptions.ProjectRuntimeException;
import project.logic.common.utils.Serializer;
import project.logic.handlers.QueryHandler;
import project.logic.representation.Dish;
import project.network.request.Request;
import project.network.response.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class Worker implements Runnable {
    private final Socket socket;
    private final QueryHandler handler;

    Worker(@NotNull Socket socket, @NotNull QueryHandler handler) {
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
                List<Dish> dishes = StreamSupport.stream(handler.handle(request.getQuery()).spliterator(), false)
                        .sorted(QueryHandler.Result.comparingByRank().reversed())
                        .limit(1)
                        .map(QueryHandler.Result::getDish)
                        .collect(Collectors.toList());
                response = Response.success(request.getMessage(), dishes);
            } catch (ProjectRuntimeException e) {
                String template = "Failed while processing data for %s: %s";
                response = Response.failure(String.format(template, request.getQuery(), e.getMessage()));
            }

            Serializer.serializeJson(s.getOutputStream(), response);
        } catch (IOException e) {
            throw new RuntimeException("Error while processing request", e);
        }
    }
}
