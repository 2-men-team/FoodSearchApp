package ua.kpi.restaurants;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import ua.kpi.restaurants.data.DataBase;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.logic.strategies.handling.AnalyticalHandler;
import ua.kpi.restaurants.logic.strategies.preprocessing.Preprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.QueryPreprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.QueryDenoiser;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.SpellingCorrector;
import ua.kpi.restaurants.network.server.Server;
import ua.kpi.restaurants.test.Client;
import ua.kpi.restaurants.test.Test;

import java.util.Set;

public final class Main {
  @Command(description = {
      "%nSimple solution that has Server, Client and Test modes%n",
      "Do not forget to specify properties file using%n" +
          "'-Dua.kpi.restaurants.Config.properties=<path-to-file>'%n"
  })
  private static final class Arguments {
    @Option(names = {"-p", "--port"}, description = "Server port", showDefaultValue = Visibility.ALWAYS)
    private int port = 9991;

    @Option(
        names = {"-m", "--mode"},
        description = {"Program mode", "  Available modes: ${COMPLETION-CANDIDATES}"},
        showDefaultValue = Visibility.ALWAYS
    )
    private Mode mode = Mode.SERVER;

    @Option(names = {"-h", "--help"}, usageHelp = true)
    private boolean helpRequested;

    int getPort() {
      return port;
    }

    Mode getMode() {
      return mode;
    }

    boolean isHelpRequested() {
      return helpRequested;
    }

    enum Mode {
      SERVER,
      CLIENT,
      TEST
    }
  }

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
        .setSpellCorrector(new SpellingCorrector(correctWords, denoiser, similarities))
        .setStemmer(Config.getInstance().getLanguageProperties().getStemmer());

    AnalyticalHandler handler = new AnalyticalHandler(builder, DataBase.getInstance().getData());

    switch (arguments.getMode()) {
      case SERVER:
        new Server(handler, arguments.getPort()).start();
        break;
      case CLIENT:
        Location location = new Location(43.45663, 42.253534);
        new Client(null, location, "localhost", arguments.getPort()).start();
        break;
      case TEST:
        Test.execute(handler);
        break;
    }
  }
}
