package ua.kpi.restaurants;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import ua.kpi.restaurants.data.Config;
import ua.kpi.restaurants.data.DataBase;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;
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
import java.util.logging.Logger;

public final class Main {
  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
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

    private enum Mode {
      SERVER,
      CLIENT,
      TEST
    }

    @Override
    public String toString() {
      return String.format("Arguments: port=%d mode=%s help=%b", port, mode, helpRequested);
    }
  }

  public static void main(String[] args) {
    LOGGER.entering(Main.class.getName(), "main");
    Arguments arguments = CommandLine.populateCommand(new Arguments(), args);

    LOGGER.finest(String.format("Have CLI arguments: %s", arguments));
    if (arguments.helpRequested) {
      CommandLine.usage(arguments, System.out);
      return;
    }

    try {
      Set<String> correctWords = DataBase.getInstance().getData().keySet();
      QueryDenoiser denoiser = new QueryDenoiser(DataBase.getInstance().getStopWords());
      SimilaritySet<String> similarities = DataBase.getInstance().getSimilarities();

      Preprocessor.Builder builder = new QueryPreprocessor.Builder()
          .setDenoiser(denoiser)
          .setSpellCorrector(new SpellingCorrector(correctWords, denoiser, similarities))
          .setStemmer(Config.getInstance().getLanguage().getProperties().getStemmer());

      AnalyticalHandler handler = new AnalyticalHandler(builder, DataBase.getInstance().getData());

      switch (arguments.mode) {
        case SERVER:
          new Server(handler, arguments.port).start();
          break;
        case CLIENT:
          Location location = new Location(43.45663, 42.253534);
          new Client(null, location, "localhost", arguments.port).start();
          break;
        case TEST:
          Test.execute(handler);
          break;
      }
    } catch (ProjectRuntimeException e) {
      LOGGER.warning("Caught ProjectRuntimeException:\n" + e);
    }

    LOGGER.exiting(Main.class.getName(), "main");
  }
}
