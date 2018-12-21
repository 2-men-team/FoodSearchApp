package ua.kpi.restaurants.test.unittest;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * The {@code TestRunner} class provides testing
 * client for major application components.
 */

public class TestRunner {
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(TestSuit.class);

    System.out.println("Total tests passed: " + result.getRunCount());
    result.getFailures().forEach(System.out::println);
  }
}
