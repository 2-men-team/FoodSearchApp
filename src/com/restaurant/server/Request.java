package com.restaurant.server;

import com.restaurant.search.Point;
import java.io.Serializable;

public final class Request implements Serializable {
    private String name;
    private String query;
    private int id;
    private final Point point;

    public Request(String name, String query, int id, Point point) {
        this.name = name;
        this.query = query;
        this.id = id;
        this.point = point;
    }

    public Request(String name, String query, int id, double lat, double lon) {
        this.name = name;
        this.query = query;
        this.id = id;
        this.point = new Point(lat, lon);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public Point getPoint() {
        return point;
    }
}
