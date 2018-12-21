package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.Denoiser;
import ua.kpi.restaurants.logic.strategies.preprocessing.routines.QueryDenoiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static ua.kpi.restaurants.logic.strategies.preprocessing.QueryPreprocessor.DELIMITERS;

/**
 * The {@code DenoiserTest} represents testing class.
 * It tests {@link Denoiser} {@code apply} method.
 */
@RunWith(Parameterized.class)
public final class DenoiserTest {
  private static Denoiser denoiser;

  private final String actual;
  private final String expected;

  /**
   * Initializes test parameters.
   *
   * @param actual actual string
   * @param expected expected string
   */
  public DenoiserTest(@NotNull String actual, @NotNull String expected) {
    this.actual = actual;
    this.expected = expected;
  }

  /**
   * Instantiates denoiser object.
   *
   * @throws IOException if test {@code STOP_WORDS} file is missing.
   */
  @BeforeClass
  public static void load() throws IOException {
    denoiser = new QueryDenoiser(TestHelper.processStopWords(TestHelper.STOP_WORDS));
  }

  /**
   * Creates testing data.
   *
   * @return collection {@code Collection<Object[]>} of objects
   *         for testing
   */
  @NotNull
  @Parameters
  public static Collection<Object[]> data() {
    Object[][] tests = {
        {"", ""},
        {"and I then me aaa","aaa"},
        {"jvm","jvm"},
        {"sample test case with some noise","sample test case noise"},
        {"and and then using usually wish who x", ""},
        {"collection x y", "collection"},
        {"would spell no jack","spell jack"},
        {"rang in the deeps of moria and then fell back into the dark","rang deeps moria fell dark"},
        {"aegis of immortal within the lived ones","aegis immortal lived"},
        {"assfsd kandkas asdmsa sdfm dfmkdsf","assfsd kandkas asdmsa sdfm dfmkdsf"}
    };

    return Arrays.asList(tests);
  }

  /**
   * Tests {@link Denoiser} {@code apply} function.
   */
  @Test
  public void testDenoiser() {
    String s = TestHelper.join(denoiser.apply(Arrays.asList(actual.trim().toLowerCase().split(DELIMITERS))));
    assertEquals(expected, s);
  }
}
