package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.kpi.restaurants.logic.common.algorithms.BKTreeSet;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;
import ua.kpi.restaurants.logic.common.utils.metrics.Levenstein;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.runners.Parameterized.Parameters;

/**
 *  The {@code BKTreeContainsTest} represents test class.
 *  It tests {@link BKTreeSet} {@code contains} method.
 */
@RunWith(Parameterized.class)
public class BKTreeSetContainsTest {
  private static BKTreeSet tree;
  private final static Set<String> WORDS;

  private final String word;

  static {
    try {
      WORDS = TestHelper.processStopWords(TestHelper.STOP_WORDS);
    } catch (IOException e) {
      throw new ProjectRuntimeException("Error while initializing test cases", e);
    }
  }

  /**
   * Initializes test parameters
   *
   * @param word test word
   */
  public BKTreeSetContainsTest(@NotNull String word) {
    this.word = word;
  }

  /**
   * Initializes {@link BKTreeSet} with {@code WORDS}
   */
  @BeforeClass
  public static void load() {
    tree = new BKTreeSet(new Levenstein());
    tree.addAll(WORDS);
  }

  /**
   * Creates collection of objects for testing.
   *
   * @return {@code Collection<Object[]>} collection of objects
   */
  @NotNull
  @Parameters
  public static Collection<Object[]> data() {
    return WORDS.stream()
        .map(word -> new Object[]{word})
        .collect(Collectors.toList());
  }

  /**
   * Tests {@link BKTreeSet} {@code contains} method.
   */
  @Test
  public void testBKTreeSet() {
    assertTrue(tree.contains(word));
  }
}
