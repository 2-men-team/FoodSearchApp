package ua.kpi.restaurants.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.test.Test;
import ua.kpi.restaurants.test.unittest.TestRunner;

/**
 * Command to execute simple testing using {@link Test} class
 */
@Command(name = "test", description = "Simple command line test", mixinStandardHelpOptions = true)
public final class TestCommand implements Runnable {
  @ParentCommand
  private RootCommand rootCommand;

  @Option(names = {"-u", "--unit"}, description = "Enable unit testing", showDefaultValue = Visibility.ALWAYS)
  private boolean isWithUnitTest = false;

  /**
   * This method is called when this command is selected from the CLI.
   * Delegates task to {@link Test#execute(HandlingStrategy)}.
   */
  @Override
  public void run() {
    if (isWithUnitTest) {
      System.out.println("Performing unit testing");
      TestRunner.execute();
    }

    Test.execute(rootCommand.getStrategy());
  }
}
