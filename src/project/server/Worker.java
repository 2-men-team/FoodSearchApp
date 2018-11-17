package project.server;

import org.jetbrains.annotations.NotNull;
import project.logic.common.utils.Serializer;
import project.logic.handlers.QueryHandler;
import project.logic.representation.Dish;

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
            List<Dish> dishes = StreamSupport.stream(handler.handle(request.getQuery()).spliterator(), false)
                    .sorted(QueryHandler.Result.comparingByRank().reversed())
                    .limit(20)
                    .map(QueryHandler.Result::getDish)
                    .collect(Collectors.toList());

            Serializer.serialize(s.getOutputStream(), new Response(request.getName(), dishes));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
