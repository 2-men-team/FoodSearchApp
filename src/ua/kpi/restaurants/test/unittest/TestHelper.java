package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public final class TestHelper {
  public static final String STOP_WORDS = "resources/stop_words_en.txt";

  private TestHelper() { }

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

  @NotNull
  public static String join(@NotNull List<String> list) {
    return String.join(" ", list);
  }
}
