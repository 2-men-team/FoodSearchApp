package project.server;

import org.jetbrains.annotations.NotNull;
import project.Config;
import project.logic.handlers.QueryHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server extends Thread {
    private static final int POOL_SIZE = 15;

    private final QueryHandler handler;
    private final ExecutorService pool;

    public Server(@NotNull QueryHandler handler) {
        super();
        this.handler = handler;
        this.pool = Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(Config.PORT)) {
            // noinspection InfiniteLoopStatement
            while (true) pool.execute(new Worker(server.accept(), handler));
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
