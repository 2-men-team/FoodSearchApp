package com.restaurant.data;

import com.restaurant.Config;
import com.restaurant.search.ResolveHandler;

import java.io.*;

public final class Serializer {
    // Do not instantiate
    private Serializer() { }

    public static void serialize(final ResolveHandler resolver) {
        try {
            FileOutputStream file = new FileOutputStream(Config.SYSTEM_PATH);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(resolver);
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static ResolveHandler deserialize(final String path) {
        ResolveHandler resolver;

        try {
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);

            resolver = (ResolveHandler) in.readObject();
            in.close();
            file.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return resolver;
    }

    public static void printRestaurants(final Restaurant[] restaurants) {
        System.out.println("Results.");
        for (Restaurant r : restaurants) {
            if (r != null) {
                System.out.println("Restaurant :" + r.name + ". Food matches : " + r.rank() + ".");
                System.out.println("Dishes.");
                r.printDishes();
            }
        }
    }
}
