package ua.kpi.restaurants.test.unittest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The {@code TestSuit} class performs testing of major application components.
 *
 * It tests the following classes:
 *
 * <ul>
 *    <li>{@link ua.kpi.restaurants.logic.common.utils.Serializer}</li>
 *    <li>{@link ua.kpi.restaurants.logic.common.utils.metrics.Levenstein}</li>
 *    <li>{@link ua.kpi.restaurants.logic.strategies.preprocessing.routines.Denoiser}</li>
 *    <li>{@link ua.kpi.restaurants.logic.common.algorithms.BKTreeSet}</li>
 * </ul>
 */
@RunWith(Suite.class)
@SuiteClasses({
    NativeSerializerTest.class,
    LevensteinTest.class,
    DenoiserTest.class,
    BKTreeSetSimilarityTest.class,
    BKTreeSetContainsTest.class
})

public class TestSuit {
  /**
   * Marks the start of testing process.
   */
  @BeforeClass
  public static void startTest() {
    System.out.println("Starting tests.");
  }

  /**
   * Marks the end of testing process.
   */
  @AfterClass
  public static void endTest() {
    System.out.println("Tests passed.");
  }
}


