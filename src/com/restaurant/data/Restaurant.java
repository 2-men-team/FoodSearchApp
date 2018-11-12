package com.restaurant.data;

import com.restaurant.Config;
import com.restaurant.search.Point;

import java.io.Serializable;
import java.util.*;

public final class Restaurant implements Serializable {
    final String name;
    final String location;
    final Point point;

    final List<Dish> dishes;
    Set<Dish> searched;
    Set<Dish> searchedAll;

    private int total = 0;

    public Restaurant(String name, String location, double latitude, double longitude, Dish[] dishes) {
        this.name = name;
        this.location = location;
        this.point = new Point(latitude, longitude);
        this.dishes = new ArrayList<>(Arrays.asList(dishes));
        this.searched = new HashSet<>();
        this.searchedAll = new HashSet<>();
    }

    public int rank() {
        return total;
    }

    public int rank(String[] query) {
        total = matchesOne(query) + matchesAll(query);
        return total;
    }

    public int matchesAll(String[] query) {
        int matches = 0;
        searchedAll = new HashSet<>();

        for (Dish dish : dishes) {
            if (dish.containsAll(query)) {
                searchedAll.add(dish);
                matches += Config.COEF * query.length;
            }
        }
        return matches;
    }

    public int matchesOne(String[] query) {
        int matches = 0;
        searched = new HashSet<>();

        for (String key : query) {
            for (Dish dish : dishes) {
                if (dish.contains(key) && !searched.contains(dish)) {
                    searched.add(dish);
                    matches++;
                }
            }
        }
        return matches;
    }

    public void printDishes() {
        int print = Config.DISH_SIZE;

        if (searchedAll.size() != 0) {
            for (Dish dish : searchedAll) {
                System.out.println("Dish : " + dish.name + ". At price : " + dish.price + "$.");
                if ((print--) == 0) return;
            }
        } else {
            for (Dish dish : searched) {
                System.out.println("Dish : " + dish.name + ". At price : " + dish.price + "$.");
                if ((print--) == 0) return;
            }
        }
    }

    public double distanceTo(Point that) {
        return this.point.distanceTo(that);
    }

    public List<Dish> getDishes() {
        return dishes;
    }
}
