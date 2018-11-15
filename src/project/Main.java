package project;

import project.logic.Config;
import project.logic.handlers.QueryHandler;

import java.util.Scanner;
import java.util.stream.StreamSupport;

public final class Main {
    public static void main(String... args) throws InstantiationException, IllegalAccessException {
        QueryHandler handler = QueryHandler.Factory.getByMethodType(Config.DEFAULT_METHOD_TYPE);

        try (Scanner scanner = new Scanner(System.in, "utf-8")) {
            System.out.print("> ");
            while (scanner.hasNext()) {
                StreamSupport.stream(handler.handle(scanner.nextLine()).spliterator(), false)
                        .sorted(QueryHandler.Result.comparingByRank().reversed())
                        .limit(20)
                        .forEach(System.out::println);
                System.out.print("> ");
            }
        }
    }
}
