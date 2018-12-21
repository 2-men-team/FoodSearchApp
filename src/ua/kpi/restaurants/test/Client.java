package ua.kpi.restaurants.test;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;
import ua.kpi.restaurants.logic.common.utils.Serializer;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.network.data.Ordering;
import ua.kpi.restaurants.network.data.Request;
import ua.kpi.restaurants.network.data.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Simple client implementation to perform dynamic testing
 *
 * This class is intended to be used with {@link ua.kpi.restaurants.network.server.Server}
 */
public final class Client {
  private Client() {}

  private static void validatePort(int port) {
    if (!(port >= 0 && port <= 65535)) {
      throw new IllegalArgumentException("Port is out of range [0; 65535].");
    }
  }

  /**
   * Executes this client
   *
   * It uses console window to read queries and prints results back
   *
   * @param location - location of the client (must not be {@code null})
   * @param host - server host (must not be {@code null})
   * @param port - server port
   * @param ordering - chosen ordering (must not be {@code null})
   * @param rule - chosen rule for ordering
   * @throws ProjectRuntimeException if instantiation of socket failed
   * @see Ordering
   * @see Ordering.Rule
   * @see Location
   */
  public static void execute(
      @NotNull Location location,
      @NotNull String host,
      int port,
      @NotNull Ordering ordering,
      @NotNull Ordering.Rule rule
  ) {
    Scanner scanner = new Scanner(System.in, "utf-8");
    System.out.println("Start inputting queries (press Ctrl-D to break):");
    System.out.print("> ");

    validatePort(port);
    while (scanner.hasNextLine()) {
      String query = scanner.nextLine().trim().toLowerCase();

      try (Socket socket = new Socket(host, port)) {
        Request request = new Request(query, location, ordering, rule);
        Serializer.serializeJson(socket.getOutputStream(), request);
        Response response = Serializer.deserializeJson(socket.getInputStream(), Response.class);

        if (response.getStatus() == Response.Status.FAILURE) {
          System.out.println(response.getMessage());
        } else {
          response.getData().forEach(System.out::println);
        }

        System.out.println();
        System.out.print("> ");
      } catch (IOException e) {
        throw new ProjectRuntimeException("Error while creating a socket", e);
      }
    }
  }
}
