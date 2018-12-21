package ua.kpi.restaurants.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.test.Test;

/**
 * Command to execute simple testing using {@link Test} class
 */
@Command(name = "test", description = "Simple command line test", mixinStandardHelpOptions = true)
public final class TestCommand implements Runnable {
  @ParentCommand
  private RootCommand rootCommand;

  /**
   * This method is called when this command is selected from the CLI.
   * Delegates task to {@link Test#execute(HandlingStrategy)}.
   */
  @Override
  public void run() {
    Test.execute(rootCommand.getStrategy());
  }
}
