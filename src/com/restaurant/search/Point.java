package com.restaurant.search;

import java.io.Serializable;

public final class Point implements Serializable {
    public final double lat;
    public final double lon;

    public Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Point(final Point p) {
        this.lat = p.lat;
        this.lon = p.lon;
    }

    public double distanceTo(final Point that) {
        double a = Math.abs(this.lat - that.lat);
        double b = Math.abs(this.lon - that.lon);
        return Math.hypot(a, b);
    }
}
