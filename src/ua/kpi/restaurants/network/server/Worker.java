package ua.kpi.restaurants.network.server;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;
import ua.kpi.restaurants.logic.common.utils.Serializer;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy.Result;
import ua.kpi.restaurants.network.data.Request;
import ua.kpi.restaurants.network.data.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

final class Worker implements Runnable {
  private final Socket socket;
  private final HandlingStrategy handler;

  Worker(@NotNull Socket socket, @NotNull HandlingStrategy handler) {
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
        List<Result> data = handler.apply(request.getQuery()).stream()
            .sorted(Result.comparingByRank().reversed()).limit(20)
            .collect(Collectors.toList());

        response = Response.success(null, data);
      } catch (ProjectRuntimeException e) {
        String template = "Failed while processing data for %s: %s";
        response = Response.failure(String.format(template, request.getQuery(), e.getMessage()));
      }

      Serializer.serializeJson(s.getOutputStream(), response);
    } catch (IOException e) {
      throw new RuntimeException("Error while processing data", e);
    }
  }
}
