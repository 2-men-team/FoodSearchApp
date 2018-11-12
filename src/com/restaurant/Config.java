package com.restaurant;

public final class Config {
    // Do not instantiate
    private Config() { }

    public static final String NOISE_PATH = "/home/napoleon/IdeaProjects/RestaurantsSearch/data/stop-word-list.csv";
    public static final String DATA_PATH = "/home/napoleon/IdeaProjects/RestaurantsSearch/data/data_delim.csv";
    public static final String SYSTEM_PATH = "/home/napoleon/IdeaProjects/Restaurant/system.ser";
    public static final String FREQ_PATH = "/home/napoleon/IdeaProjects/RestaurantsSearch/data/dish_freq.csv";
    public static final String HOST = "localhost";
    public static final int PORT = 9991;
    public static final int PQ_SIZE = 100;
    public static final int SIZE = 8;
    public static final int TOLERANCE = 2;
    public static final int DISH_SIZE = 5;
    public static final int COEF = 2;
}
