package project.network.server;

import org.jetbrains.annotations.NotNull;
import project.logic.common.exceptions.ProjectRuntimeException;
import project.logic.common.utils.Serializer;
import project.logic.handlers.QueryHandler;
import project.logic.representation.Dish;
import project.network.request.Request;
import project.network.response.Failure;
import project.network.response.Response;
import project.network.response.Success;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class Worker implements Runnable {
    private final Socket socket;
    private final QueryHandler handler;

    public Worker(@NotNull Socket socket, @NotNull QueryHandler handler) {
        this.socket  = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        try (Socket s = socket) {
            Request request = (Request) Serializer.deserialize(s.getInputStream());
            System.out.println(request);

            Response response;
            try {
                List<Dish> dishes = StreamSupport.stream(handler.handle(request.getQuery()).spliterator(), false)
                        .sorted(QueryHandler.Result.comparingByRank().reversed())
                        .limit(20)
                        .map(QueryHandler.Result::getDish)
                        .collect(Collectors.toList());
                response = new Success(request.getMessage(), dishes);
            } catch (ProjectRuntimeException e) {
                String template = "Failed while processing data for %s: %s";
                response = new Failure(String.format(template, request.getQuery(), e.getMessage()));
            }

            Serializer.serialize(s.getOutputStream(), response);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error while processing request", e);
        }
    }
}
