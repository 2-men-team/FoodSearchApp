package ua.kpi.restaurants;

import picocli.CommandLine;
import ua.kpi.restaurants.data.DataBase;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.strategies.handling.AnalyticalHandler;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.logic.strategies.preprocessing.Preprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.QueryPreprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.QueryDenoiser;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.SpellCorrector;
import ua.kpi.restaurants.network.server.Server;
import ua.kpi.restaurants.test.Client;
import ua.kpi.restaurants.test.Test;

import java.util.Set;

public final class Main {
  public static void main(String[] args) {
    Arguments arguments = CommandLine.populateCommand(new Arguments(), args);

    if (arguments.isHelpRequested()) {
      CommandLine.usage(arguments, System.out);
      return;
    }

    Set<String> correctWords = DataBase.getInstance().getData().keySet();
    QueryDenoiser denoiser = new QueryDenoiser(DataBase.getInstance().getStopWords());
    SimilaritySet<String> similarities = DataBase.getInstance().getSimilarities();

    Preprocessor.Builder builder = new QueryPreprocessor.Builder()
        .setDenoiser(denoiser)
        .setSpellCorrector(new SpellCorrector(correctWords, denoiser, similarities))
        .setStemmer(Config.getInstance().getLanguageProperties().getStemmer());

    HandlingStrategy strategy = new AnalyticalHandler(builder, DataBase.getInstance().getData());

    switch (arguments.getMode()) {
      case SERVER:
        new Server(strategy, arguments.getPort()).start();
        break;
      case CLIENT:
        Location location = new Location(43.45663, 42.253534);
        new Client(null, location, "localhost", arguments.getPort()).start();
        break;
      case TEST:
        Test.execute(strategy);
        break;
    }
  }
}
