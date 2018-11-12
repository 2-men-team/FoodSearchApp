package com.restaurant.server;

import com.restaurant.Config;
import com.restaurant.data.Serializer;
import com.restaurant.search.Point;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class Client extends Thread {
    private final Point point;
    private final int id;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Client(int id, double lat, double lon) {
        super();
        this.id = id;
        this.point = new Point(lat, lon);
    }

    @Override
    public void run() {
        Socket socket;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            for (;;) {
                socket = new Socket(Config.HOST, Config.PORT);
                out = new ObjectOutputStream(socket.getOutputStream());

                System.out.println("> ");
                String query = reader.readLine().trim().toLowerCase();
                if (query.equals("exit")) break;

                out.writeObject(new Request("Request", query, 0, point));
                out.flush();

                in = new ObjectInputStream(socket.getInputStream());
                Response response = (Response) in.readObject();

                // show response
                Serializer.printRestaurants(response.getRestaurants());

                // Close connections
                in.close();
                out.close();
                socket.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
