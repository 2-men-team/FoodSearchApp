package ua.kpi.restaurants.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import ua.kpi.restaurants.logic.strategies.handling.HandlingStrategy;
import ua.kpi.restaurants.network.server.Server;

/**
 * Command to execute a {@link Server}.
 *
 * It accepts two parameters:
 * <ol>
 *   <li>port - to run the server on</li>
 *   <li>poolSize of the thread pool</li>
 * </ol>
 */
@Command(name = "server", description = "Server", mixinStandardHelpOptions = true)
public final class ServerCommand implements Runnable {
  @ParentCommand
  private RootCommand rootCommand;

  @Option(names = {"-s", "--size"}, description = "Size of thread pool", showDefaultValue = Visibility.ALWAYS)
  private int poolSize = 15;

  @Option(names = {"-p", "--port"}, description = "Server port", showDefaultValue = Visibility.ALWAYS)
  private int port = 9991;

  /**
   * This method is called when this command is selected from the CLI.
   * Delegates task to {@link Server#execute(HandlingStrategy, int, int)}.
   */
  @Override
  public void run() {
    Server.execute(rootCommand.getStrategy(), port, poolSize);
  }
}
