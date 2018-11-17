package project;

import project.logic.handlers.QueryHandler;

public final class Config {
    private Config() { }

    public static final String STOP_WORDS_PATH = "data/stop_words.txt";
    public static final String DATA_SET_PATH   = "data/modified.csv";
    public static final String DB_PATH         = "data/db.ser";
    public static final QueryHandler.MethodType DEFAULT_METHOD_TYPE = QueryHandler.MethodType.ANALYTICAL;

    public static final int PORT = 9991;
    public static final String HOST = "localhost";
}
