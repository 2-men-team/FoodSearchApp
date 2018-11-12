package com.restaurant.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public final class Dish implements Serializable {
    final String name;
    final double price;
    private List<String> parts;

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
        this.parts = Arrays.asList(name.split(" "));
    }

    public boolean contains(String word) {
        return parts.contains(word);
    }

    public boolean containsAll(String[] query) {
        for (String key : query)
            if (!parts.contains(key)) return false;
        return true;
    }
}
