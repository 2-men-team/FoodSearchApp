package ua.kpi.restaurants.test;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy.Result;

import java.util.Scanner;

public final class Test {
  public static void execute(@NotNull HandlingStrategy strategy) {
    Scanner scanner = new Scanner(System.in, "utf-8");
    System.out.println("Start inputting queries (press Ctrl-D to break):");
    System.out.print("> ");

    while (scanner.hasNextLine()) {
      strategy.apply(scanner.nextLine().trim().toLowerCase()).stream()
          .sorted(Result.comparingByRank().reversed()).limit(20)
          .forEach(result -> {
            System.out.println(result.getRestaurant());
            result.getDishes().forEach(System.out::println);
            System.out.println();
          });
      System.out.print("> ");
    }
  }
}
