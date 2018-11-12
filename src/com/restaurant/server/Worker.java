package com.restaurant.server;

import com.restaurant.search.ResolveHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public final class Worker implements Runnable {
    private Socket socket;
    private ResolveHandler resolver;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Worker(Socket socket, ResolveHandler resolver) {
        this.socket = socket;
        this.resolver = resolver;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            Request request = (Request) in.readObject();
            out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(new Response(
                    request.getName(),
                    resolver.search(request.getQuery(), request.getPoint()),
                    request.getId())
            );

            in.close();
            out.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
