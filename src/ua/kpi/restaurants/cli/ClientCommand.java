package ua.kpi.restaurants.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;
import ua.kpi.restaurants.logic.representation.Location;
import ua.kpi.restaurants.network.data.Ordering;
import ua.kpi.restaurants.test.Client;

/**
 * Command to execute a {@link Client} testing.
 *
 * It accepts the following parameters from CLI:
 * <ol>
 *   <li>latitude of the client's position</li>
 *   <li>longitude of the client's position</li>
 *   <li>host to connect client to</li>
 *   <li>port of the host</li>
 *   <li>{@link Ordering} strategy to be applied to results</li>
 *   <li>{@link Ordering.Rule} to be applied to results</li>
 * </ol>
 */
@Command(name = "client", description = "Simple client execution", mixinStandardHelpOptions = true)
public final class ClientCommand implements Runnable {
  @Option(names = "--lat", description = "Location latitude")
  private double lat = 43.45663;

  @Option(names = "--lon", description = "Location longitude")
  private double lon = 42.253534;

  @Option(names = {"-H", "--host"}, description = "Host to connect to", showDefaultValue = Visibility.ALWAYS)
  private String host = "localhost";

  @Option(names = {"-p", "--port"}, description = "Port to connect to", showDefaultValue = Visibility.ALWAYS)
  private int port = 9991;

  @Option(
      names = {"-o", "--order"},
      description = {"Order of the result produced", "  Available orders: ${COMPLETION-CANDIDATES}"},
      showDefaultValue = Visibility.ALWAYS)
  private Ordering ordering = Ordering.BY_RANK;

  @Option(
      names = {"-r", "--rule"},
      description = {"Ordering rule", "  Available rules: ${COMPLETION-CANDIDATES}"},
      showDefaultValue = Visibility.ALWAYS)
  private Ordering.Rule rule = Ordering.Rule.REVERSED;

  /**
   * This method is called when this command is selected from the CLI.
   * Delegates task to {@link Client#execute(Location, String, int, Ordering, Ordering.Rule)}.
   */
  @Override
  public void run() {
    Client.execute(new Location(lat, lon), host, port, ordering, rule);
  }
}
