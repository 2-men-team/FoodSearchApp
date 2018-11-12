package com.restaurant.server;

import com.restaurant.Config;
import com.restaurant.data.Serializer;
import com.restaurant.search.ResolveHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server extends Thread {
    public ResolveHandler resolver;
    private Socket socket;
    private ExecutorService tpool = Executors.newFixedThreadPool(15);

    public Server(ResolveHandler resolver) {
        super();
        this.resolver = resolver;
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(Config.PORT);
            while (true) {
                try {
                    socket = server.accept();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }
                tpool.execute(new Worker(socket, resolver));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ResolveHandler resolver = Serializer.deserialize(Config.SYSTEM_PATH);

        // run server and clients ( not very convenient to run on 1 machine )
        new Server(resolver).start();
        new Client(0, 40.5, -73.2).start();
        //new Client(1, 40.3, -72.7).start();
        //new Client(2, 40.6, -72.9).start();
    }
}
