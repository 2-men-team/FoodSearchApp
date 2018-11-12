package com.restaurant.metrics;

@FunctionalInterface
public interface Metric {
    int compute(String a, String b);
}
