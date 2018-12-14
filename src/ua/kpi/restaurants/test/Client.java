package ua.kpi.restaurants.test;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.kpi.restaurants.Config;
import ua.kpi.restaurants.logic.common.utils.Serializer;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.network.data.Request;
import ua.kpi.restaurants.network.data.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public final class Client extends Thread {
  private final String name;
  private final Location location;
  private final String host;
  private final int port;

  public Client(@Nullable String name, @NotNull Location location, @NotNull String host, int port) {
    super();
    this.name = name;
    this.location = location;
    this.host = host;
    this.port = port;
  }

  @Override
  public void run() {
    Scanner scanner = new Scanner(System.in, "utf-8");
    System.out.println("Start inputting queries (press Ctrl-D to break):");
    System.out.print("> ");

    while (scanner.hasNextLine()) {
      String query = scanner.nextLine().trim().toLowerCase();

      try (Socket socket = new Socket(host, port)) {
        Serializer.serializeJson(socket.getOutputStream(), new Request(name, query, location));
        Response response = Serializer.deserializeJson(socket.getInputStream(), Response.class);

        if (response.getStatus() == Response.Status.FAILURE) {
          System.out.println(response.getMessage());
        } else {
          response.getData().stream()
              .sorted(HandlingStrategy.Result.comparingByRank().reversed())
              .forEach(result -> {
                System.out.println(result.getRestaurant());
                result.getDishes().forEach(System.out::println);
                System.out.println();
              });
        }

        System.out.print("> ");
      } catch (IOException e) {
        throw new RuntimeException("Error while sending a data", e);
      }
    }
  }
}
