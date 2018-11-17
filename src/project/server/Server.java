package project.server;

import org.jetbrains.annotations.NotNull;
import project.Config;
import project.logic.handlers.QueryHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server extends Thread {
    private final QueryHandler handler;
    private final ExecutorService pool = Executors.newFixedThreadPool(15);

    public Server(@NotNull QueryHandler handler) {
        super();
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(Config.PORT)) {
            while (true) {
                Socket socket;

                try {
                    socket = server.accept();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }

                pool.execute(new Worker(socket, handler));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) throws InstantiationException, IllegalAccessException {
        QueryHandler handler = QueryHandler.Factory.getByMethodType(Config.DEFAULT_METHOD_TYPE);
        System.out.println("Database loaded...");
        new Server(handler).start();
    }
}
