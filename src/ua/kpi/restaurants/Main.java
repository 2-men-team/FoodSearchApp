package ua.kpi.restaurants;

import picocli.CommandLine;
import ua.kpi.restaurants.cli.RootCommand;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Launcher class
 *
 * It uses CLI to initialize the application
 */
public final class Main {
  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  /** Entry point */
  public static void main(String[] args) {
    LOGGER.entering(Main.class.getName(), "main");

    try {
      LOGGER.fine("Arguments passed: " + Arrays.toString(args));
      CommandLine.run(new RootCommand(), args);
    } catch (ProjectRuntimeException e) {
      LOGGER.warning("Caught ProjectRuntimeException:\n" + e);
    }

    LOGGER.exiting(Main.class.getName(), "main");
  }
}
