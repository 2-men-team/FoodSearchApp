package com.restaurant.server;

import com.restaurant.data.Restaurant;
import java.io.Serializable;

public final class Response implements Serializable {
    private String name;
    private Restaurant[] restaurants;
    private int id;

    Response(String name, Restaurant[] restaurants, int id) {
        this.name = name;
        this.restaurants = restaurants;
        this.id = id;
    }

    public Restaurant[] getRestaurants() {
        return restaurants;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}