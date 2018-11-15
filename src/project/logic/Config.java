package project.logic;

import project.logic.handlers.QueryHandler;

public final class Config {
    private Config() { }

    static final String STOP_WORDS_PATH = "data/stop_words.txt";
    static final String DATA_SET_PATH   = "data/modified.csv";
    static final String DB_PATH         = "data/db.ser";
    public static final QueryHandler.MethodType DEFAULT_METHOD_TYPE = QueryHandler.MethodType.ANALYTICAL;
}
