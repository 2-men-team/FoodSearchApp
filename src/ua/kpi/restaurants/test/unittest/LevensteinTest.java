package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;
import ua.kpi.restaurants.logic.common.utils.metrics.Levenstein;
import ua.kpi.restaurants.logic.common.utils.metrics.Metric;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public final class LevensteinTest {
  public static final String TEST_FILE = "resources/tests/levenstein.csv";
  public static final int N = 1000;

  private final String a;
  private final String b;
  private final int distance;

  public LevensteinTest(@NotNull String a, @NotNull String b, int distance) {
    this.a = a;
    this.b = b;
    this.distance = distance;
  }

  @NotNull
  @Parameters
  public static Collection<Object[]> data() {
    List<Object[]> tests = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(TEST_FILE))) {
      while (scanner.hasNextLine()) {
        String[] test = scanner.nextLine().split(",");
        tests.add(new Object[]{test[0], test[1], Integer.parseInt(test[2])});
      }
    } catch (FileNotFoundException e) {
      throw new ProjectRuntimeException("Error while initializing test cases", e);
    }

    return tests;
  }

  @Test
  public void testLevensteinMetric() {
    Metric<String, ?> metric = new Levenstein();
    assertEquals(distance, metric.apply(a, b));
  }
}
