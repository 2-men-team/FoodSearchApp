package project;

import project.logic.common.algorithms.SimilaritySet;
import project.logic.representation.Dish;
import project.logic.strategies.handling.HandlingStrategy.Result;
import project.logic.strategies.preprocessing.Preprocessor;
import project.logic.strategies.preprocessing.QueryPreprocessor;
import project.logic.strategies.preprocessing.routines.Denoiser;
import project.logic.strategies.preprocessing.routines.QueryDenoiser;
import project.logic.strategies.preprocessing.routines.SpellCorrector;
import project.logic.strategies.preprocessing.routines.Stemmer;
import project.logic.strategies.handling.AnalyticalHandler;

import java.util.Scanner;
import java.util.Set;

public final class Main {
    public static void main(String[] args) {
        Set<String> correctWords = DataBase.getInstance().getData().keySet();
        Denoiser denoiser = new QueryDenoiser(DataBase.getInstance().getStopWords());
        SimilaritySet<String> similarities = DataBase.getInstance().getSimilarities();

        Preprocessor.Builder builder = new QueryPreprocessor.Builder()
                .setDenoiser(denoiser)
                .setSpellCorrector(new SpellCorrector(correctWords, denoiser, similarities))
                .setStemmer(Stemmer.ENGLISH);

        AnalyticalHandler handler = new AnalyticalHandler(builder, DataBase.getInstance().getData());

        Scanner scanner = new Scanner(System.in, "utf-8");
        System.out.println("Start inputting queries (press Ctrl-D to break):");
        System.out.print("> ");
        while (scanner.hasNextLine()) {
            handler.apply(scanner.nextLine().trim().toLowerCase())
                    .stream()
                    .sorted(Result.<Dish>comparingByRank().reversed())
                    .limit(20)
                    .forEach(System.out::println);
            System.out.print("> ");
        }
    }
}
