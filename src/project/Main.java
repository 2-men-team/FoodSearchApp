package project;

import project.logic.common.algorithms.SimilaritySet;
import project.logic.common.utils.parsers.CommonQueryParser;
import project.logic.common.utils.parsers.QueryParser;
import project.logic.common.utils.preprocessors.denoiser.Denoiser;
import project.logic.common.utils.preprocessors.denoiser.QueryDenoiser;
import project.logic.common.utils.preprocessors.mappers.SpellCorrector;
import project.logic.common.utils.preprocessors.mappers.Stemmer;
import project.logic.handlers.AnalyticalHandler;
import project.logic.handlers.QueryHandler;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.StreamSupport;

public final class Main {
    public static void main(String... args) {
        Set<String> correctWords = DataBase.getInstance().getData().keySet();
        Denoiser denoiser = new QueryDenoiser(DataBase.getInstance().getStopWords());
        SimilaritySet<String> similarities = DataBase.getInstance().getSimilarities();

        QueryParser parser = new CommonQueryParser.Builder()
                .setDenoiser(denoiser)
                .setSpellCorrector(new SpellCorrector(correctWords, denoiser, similarities))
                .setStemmer(new Stemmer())
                .build();

        QueryHandler handler = new AnalyticalHandler(parser);

        Scanner scanner = new Scanner(System.in, "utf-8");
        System.out.println("Start inputting queries (press Ctrl-D to break):");
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            StreamSupport.stream(handler.handle(scanner.nextLine()).spliterator(), false)
                    .sorted(QueryHandler.Result.comparingByRank().reversed())
                    .limit(20)
                    .forEach(System.out::println);
            System.out.print("> ");
        }
    }
}
