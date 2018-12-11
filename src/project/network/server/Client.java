package project.network.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.Config;
import project.logic.common.utils.Serializer;
import project.logic.representation.Location;
import project.network.data.Request;
import project.network.data.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public final class Client extends Thread {
    private final String name;
    private final Location location;

    public Client(@Nullable String name, @NotNull Location location) {
        super();
        this.name = name;
        this.location = location;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in, "utf-8");
        System.out.println("Start inputting queries (press Ctrl-D to break):");
        System.out.print("> ");

        while (scanner.hasNextLine()) {
            String query = scanner.nextLine().trim().toLowerCase();

            try (Socket socket = new Socket(Config.HOST, Config.PORT)) {
                Serializer.serializeJson(socket.getOutputStream(), new Request(name, query, location));
                Response response = Serializer.deserializeJson(socket.getInputStream(), Response.class);

                if (response.getStatus() == Response.Status.FAILURE) {
                    System.out.println(response.getMessage());
                } else {
                    System.out.println("By location:");
                    response.getDataByLocation().forEach(System.out::println);
                    System.out.println("By rank:");
                    response.getDataByRank().forEach(System.out::println);
                }

                System.out.print("> ");
            } catch (IOException e) {
                throw new RuntimeException("Error while sending a data", e);
            }
        }
    }

    public static void main(String[] args) {
        new Client(null, new Location(43.45663, 42.253534)).start();
    }
}
