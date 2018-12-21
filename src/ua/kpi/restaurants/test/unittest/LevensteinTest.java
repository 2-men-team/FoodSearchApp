package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ua.kpi.restaurants.logic.common.utils.metrics.Levenstein;
import ua.kpi.restaurants.logic.common.utils.metrics.Metric;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * The {@code LevensteinTest} represents test class.
 * It tests {@link Levenstein} {@code apply} method for correct
 * computing of Levenstein metric.
 */
@RunWith(Parameterized.class)
public final class LevensteinTest {
  private static final String TEST_FILE = "resources/tests/levenstein.csv";

  private final String a;
  private final String b;
  private final int distance;

  /**
   * Initialize test parameter
   *
   * @param a test string
   * @param b test string
   * @param distance expected Levenstein distance
   */
  public LevensteinTest(@NotNull String a, @NotNull String b, int distance) {
    this.a = a;
    this.b = b;
    this.distance = distance;
  }

  /**
   * Loads the testing data from {@code TEST_FILE} file.
   *
   * @return the collection {@code Collection<Object[]>} of objects for testing
   * @throws FileNotFoundException if {@code TEST_FILE} is missing
   */
  @NotNull
  @Parameters
  public static Collection<Object[]> data() throws FileNotFoundException {
    List<Object[]> tests = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(TEST_FILE))) {
      while (scanner.hasNextLine()) {
        String[] test = scanner.nextLine().split(",");
        tests.add(new Object[]{test[0], test[1], Integer.valueOf(test[2])});
      }
    }

    return tests;
  }

  /**
   * Tests {@link Levenstein} {@code apply} method.
   */
  @Test
  public void testLevensteinMetric() {
    Metric<String, ?> metric = new Levenstein();
    assertEquals(distance, metric.apply(a, b));
  }
}
