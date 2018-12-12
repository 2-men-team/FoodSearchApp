package project.network.server;

import org.jetbrains.annotations.NotNull;
import project.Config;
import project.DataBase;
import project.logic.common.algorithms.SimilaritySet;
import project.logic.representation.Dish;
import project.logic.strategies.handling.HandlingStrategy.Result;
import project.logic.strategies.preprocessing.Preprocessor;
import project.logic.strategies.preprocessing.QueryPreprocessor;
import project.logic.strategies.preprocessing.routines.QueryDenoiser;
import project.logic.strategies.preprocessing.routines.SpellCorrector;
import project.logic.strategies.preprocessing.routines.Stemmer;
import project.logic.strategies.handling.AnalyticalHandler;
import project.logic.strategies.handling.HandlingStrategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Server extends Thread {
    public static final int DEFAULT_POOL_SIZE = 15;

    private final HandlingStrategy<List<Result<Dish>>> handler;
    private final ExecutorService pool;

    public Server(@NotNull HandlingStrategy<List<Result<Dish>>> handler) {
        this(handler, DEFAULT_POOL_SIZE);
    }

    public Server(@NotNull HandlingStrategy<List<Result<Dish>>> handler, int poolSize) {
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
            throw new RuntimeException("Error while running a server", e);
        }
    }

    public static void main(String[] args) {
        Set<String> correctWords = DataBase.getInstance().getData().keySet();
        QueryDenoiser denoiser = new QueryDenoiser(DataBase.getInstance().getStopWords());
        SimilaritySet<String> similarities = DataBase.getInstance().getSimilarities();

        Preprocessor.Builder builder = new QueryPreprocessor.Builder()
                .setDenoiser(denoiser)
                .setSpellCorrector(new SpellCorrector(correctWords, denoiser, similarities))
                .setStemmer(Stemmer.RUSSIAN);

        System.out.println("Database loaded...");
        new Server(new AnalyticalHandler(builder, DataBase.getInstance().getData())).start();
    }
}
