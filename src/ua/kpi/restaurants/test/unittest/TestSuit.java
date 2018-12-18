package ua.kpi.restaurants.test.unittest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    NativeSerializerTest.class,
    LevensteinTest.class,
    DenoiserTest.class,
    BKTreeSetSimilarityTest.class,
    BKTreeSetContainsTest.class
})

public class TestSuit {
  @BeforeClass
  public static void startTest() {
        System.out.println("Starting tests.");
    }

  @AfterClass
  public static void endTest() {
        System.out.println("Tests passed.");
    }
}
