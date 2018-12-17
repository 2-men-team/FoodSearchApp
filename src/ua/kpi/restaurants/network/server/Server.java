package ua.kpi.restaurants.network.server;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public final class Server extends Thread {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
  public static final int DEFAULT_POOL_SIZE = 15;

  private final HandlingStrategy<Dish> handler;
  private final ExecutorService pool;
  private final int port;

  public Server(@NotNull HandlingStrategy<Dish> handler, int port) {
    this(handler, port, DEFAULT_POOL_SIZE);
  }

  public Server(@NotNull HandlingStrategy<Dish> handler, int port, int poolSize) {
    super();
    this.handler = handler;
    this.port = port;
    this.pool = Executors.newFixedThreadPool(validatePoolSize(poolSize));
  }

  private static int validatePoolSize(int size) {
    if (size <= 0) {
      RuntimeException e = new IllegalArgumentException("Pool size must be > 0.");
      LOGGER.throwing(Server.class.getName(), "validatePoolSize", e);
      throw e;
    }

    return size;
  }

  @SuppressWarnings("InfiniteLoopStatement")
  @Override
  public void run() {
    LOGGER.entering(Server.class.getName(), "run");
    try (ServerSocket server = new ServerSocket(port)) {
      LOGGER.info(String.format("Server is running at port %d...", port));

      while (true) {
        Socket socket = server.accept();
        LOGGER.finest("Have an input connection" + socket.getInetAddress().getHostAddress());
        pool.execute(new Worker(socket, handler));
      }
    } catch (IOException e) {
      LOGGER.severe("Caught IOException: " + e.getMessage());
    }

    LOGGER.exiting(Server.class.getName(), "run");
  }
}
