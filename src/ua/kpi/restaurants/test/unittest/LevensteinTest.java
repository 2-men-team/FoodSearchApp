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

@RunWith(Parameterized.class)
public final class LevensteinTest {
  public static final String TEST_FILE = "resources/tests/levenstein.csv";
  public static final int n = 1000;

  public final String a;
  public final String b;
  public int distance;

  public LevensteinTest(@NotNull String a, @NotNull String b, int distance) {
    this.a = a;
    this.b = b;
    this.distance = distance;
  }

  @Parameters
  public static Collection<Object[]> data() {
    Object[][] list = new Object[n][3];
    try (Scanner scanner = new Scanner(new File(TEST_FILE))) {
      for (int i = 0; i < n && scanner.hasNextLine(); i++) {
        String[] test = scanner.nextLine().split(",");
        list[i] = new Object[]{test[0], test[1], Integer.parseInt(test[2])};
      }
    } catch (FileNotFoundException e) {
      System.out.println("Test file not found: " + e.getMessage());
      throw new RuntimeException(e);
    }

    return Arrays.asList(list);
  }

  @Test
  public void testLevensteinMetric() {
    Metric metric = new Levenstein();
    assertEquals("Levenstein distance", distance, metric.apply(a, b));
  }
}
