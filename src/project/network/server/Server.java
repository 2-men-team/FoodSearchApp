package project.network.server;

import org.jetbrains.annotations.NotNull;
import project.Config;
import project.DataBase;
import project.logic.common.algorithms.SimilaritySet;
import project.logic.common.utils.parsers.CommonQueryParser;
import project.logic.common.utils.parsers.QueryParser;
import project.logic.common.utils.preprocessors.denoiser.QueryDenoiser;
import project.logic.common.utils.preprocessors.mappers.SpellCorrector;
import project.logic.common.utils.preprocessors.mappers.Stemmer;
import project.logic.handlers.AnalyticalHandler;
import project.logic.handlers.QueryHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server extends Thread {
    public static final int DEFAULT_POOL_SIZE = 15;

    private final QueryHandler handler;
    private final ExecutorService pool;

    public Server(@NotNull QueryHandler handler) {
        this(handler, DEFAULT_POOL_SIZE);
    }

    public Server(@NotNull QueryHandler handler, int poolSize) {
        super();
        this.handler = handler;
        this.pool = Executors.newFixedThreadPool(validatePoolSize(poolSize));
    }

    private static int validatePoolSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Pool size must be > 0.");
        }

        return size;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(Config.PORT)) {
            // noinspection InfiniteLoopStatement
            while (true) {
                pool.execute(new Worker(server.accept(), handler));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while starting a server", e);
        }
    }

    public static void main(String... args) {
        Set<String> correctWords = DataBase.getInstance().getData().keySet();
        QueryDenoiser denoiser = new QueryDenoiser(DataBase.getInstance().getStopWords());
        SimilaritySet<String> similarities = DataBase.getInstance().getSimilarities();

        QueryParser parser = new CommonQueryParser.Builder()
                .setDenoiser(denoiser)
                .setSpellCorrector(new SpellCorrector(correctWords, denoiser, similarities))
                .setStemmer(new Stemmer())
                .build();

        System.out.println("Database loaded...");
        new Server(new AnalyticalHandler(parser)).start();
    }
}
