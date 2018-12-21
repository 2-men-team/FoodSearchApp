package ua.kpi.restaurants.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import ua.kpi.restaurants.data.Config;
import ua.kpi.restaurants.data.DataBase;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.representation.Dish;
import ua.kpi.restaurants.logic.strategies.handling.AnalyticalHandler;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.logic.strategies.preprocessing.Preprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.QueryPreprocessor;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.QueryDenoiser;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.SpellingCorrector;

import java.util.Set;

/**
 * Root command for the CLI.
 *
 * It is responsible for {@link HandlingStrategy} initialization.
 */
@Command(
    name = "root",
    subcommands = {ServerCommand.class, ClientCommand.class, TestCommand.class},
    mixinStandardHelpOptions = true,
    version = "0.1")
public final class RootCommand implements Runnable {
  private final HandlingStrategy<Dish> strategy;

  /**
   * Constructs an instance of this class and initializes algorithm strategy.
   */
  public RootCommand() {
    Set<String> correctWords = DataBase.getInstance().getData().keySet();
    QueryDenoiser denoiser = new QueryDenoiser(DataBase.getInstance().getStopWords());
    SimilaritySet<String> similarities = DataBase.getInstance().getSimilarities();

    Preprocessor.Builder builder = new QueryPreprocessor.Builder()
        .setDenoiser(denoiser)
        .setSpellingCorrector(new SpellingCorrector(correctWords, denoiser, similarities))
        .setStemmer(Config.getInstance().getLanguage().getProperties().getStemmer());

    strategy = new AnalyticalHandler(builder, DataBase.getInstance().getData());
  }

  /**
   * Retrieves current algorithm strategy.
   *
   * @return retrieved strategy
   */
  public HandlingStrategy<Dish> getStrategy() {
    return strategy;
  }

  /**
   * This method is called when this command is selected from the command line.
   *
   * It just prints the help message.
   */
  @Override
  public void run() {
    CommandLine.usage(this, System.out);
  }
}
