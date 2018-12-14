package ua.kpi.restaurants;

import picocli.CommandLine.Help.Visibility;
import picocli.CommandLine.Option;

final class Arguments {
  @Option(names = {"-p", "--port"}, description = "Server port", showDefaultValue = Visibility.ALWAYS)
  private int port = 9991;

  @Option(names = {"-m", "--mode"}, description = "Program mode", showDefaultValue = Visibility.ALWAYS)
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
