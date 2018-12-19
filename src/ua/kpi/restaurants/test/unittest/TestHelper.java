package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The {@code TestHelper} class presents helper static methods
 * for data manipulation.
 */
public final class TestHelper {
  /**
   * Path to stop words file.
   */
  public static final String STOP_WORDS = "resources/stop_words_en.txt";

  private TestHelper() { }

  /**
   * Processes the given {@code filename} file and returns the set of words.
   *
   * @param filename name of the file to process
   * @return {@code Set<String>} set of processed words
   * @throws IOException if {@code filename} file is missing
   */
  @NotNull
  public static Set<String> processStopWords(@NotNull String filename) throws IOException {
    Set<String> words = new HashSet<>();

    try (Scanner scanner = new Scanner(new FileInputStream(filename), "utf-8")) {
      while (scanner.hasNext()) {
        words.add(scanner.next());
      }
    }

    return words;
  }

  /**
   * Receives list of strings and joins them with ' ' delimiter.
   *
   * @param list list to join
   * @return joined list
   */
  @NotNull
  public static String join(@NotNull List<String> list) {
    return String.join(" ", list);
  }
}
