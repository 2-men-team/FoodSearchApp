package ua.kpi.restaurants.network.server;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.Config;
import ua.kpi.restaurants.data.DataBase;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.strategies.preprocessing.Preprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.QueryPreprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.QueryDenoiser;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.SpellCorrector;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Stemmer;
import ua.kpi.restaurants.logic.strategies.handling.AnalyticalHandler;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server extends Thread {
  public static final int DEFAULT_POOL_SIZE = 15;

  private final HandlingStrategy handler;
  private final ExecutorService pool;
  private final int port;

  public Server(@NotNull HandlingStrategy handler, int port) {
    this(handler, port, DEFAULT_POOL_SIZE);
  }

  public Server(@NotNull HandlingStrategy handler, int port, int poolSize) {
    super();
    this.handler = handler;
    this.port = port;
    this.pool = Executors.newFixedThreadPool(validatePoolSize(poolSize));
  }

  private static int validatePoolSize(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("Pool size must be > 0.");
    }

    return size;
  }

  @Override
  public void run() {
    try (ServerSocket server = new ServerSocket(port)) {
      System.out.println("Server is running...");
      // noinspection InfiniteLoopStatement
      while (true) {
        pool.execute(new Worker(server.accept(), handler));
      }
    } catch (IOException e) {
      throw new RuntimeException("Error while running a server", e);
    }
  }
}
