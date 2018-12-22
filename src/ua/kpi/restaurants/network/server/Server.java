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

/**
 * Application server.
 *
 * It creates instance of {@link Worker} to handle the client connection request.
 */
public final class Server {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

  private Server() {}

  private static int validatePoolSize(int size) {
    if (size <= 0) {
      RuntimeException e = new IllegalArgumentException("Pool size must be > 0.");
      LOGGER.throwing(Server.class.getName(), "validatePoolSize", e);
      throw e;
    }

    return size;
  }

  private static int validatePort(int port) {
    if (!(port >= 0 && port <= 65535)) {
      RuntimeException e = new IllegalArgumentException("Port is out of range [0; 65535].");
      LOGGER.throwing(Server.class.getName(), "validatePort", e);
      throw e;
    }

    return port;
  }

  /**
   * Executes server
   *
   * Ownership of user connection socket is transferred to {@link Worker} instance
   *
   * Port must be in a range [0; 65535]. Pool size must be greater that 0.
   *
   * @param strategy - strategy to be used to handle user requests (must not be {@code null})
   * @param port - port to run the server on
   * @param poolSize - size of thread pool
   * @throws IllegalArgumentException if either port or poolSize are invalid
   * @see HandlingStrategy
   */
  @SuppressWarnings("InfiniteLoopStatement")
  public static void execute(@NotNull HandlingStrategy<Dish> strategy, int port, int poolSize) {
    LOGGER.entering(Server.class.getName(), "execute");

    ExecutorService pool = Executors.newFixedThreadPool(validatePoolSize(poolSize));

    try (ServerSocket server = new ServerSocket(validatePort(port))) {
      LOGGER.info(String.format("Server is running at port %d...", port));

      while (true) {
        Socket socket = server.accept();
        LOGGER.finest("Have an input connection " + socket.getInetAddress().getHostAddress());
        pool.execute(new Worker(socket, strategy));
      }
    } catch (IOException e) {
      LOGGER.severe("Caught IOException: " + e.getMessage());
    }

    LOGGER.exiting(Server.class.getName(), "execute");
  }
}
