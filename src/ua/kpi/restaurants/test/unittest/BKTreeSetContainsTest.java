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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertTrue;

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

  public BKTreeSetContainsTest(@NotNull String word) {
    this.word = word;
  }

  @BeforeClass
  public static void load() throws IOException {
    tree = new BKTreeSet(new Levenstein());
    tree.addAll(WORDS);
  }

  @NotNull
  @Parameters
  public static Collection<Object[]> data() throws IOException {
    return WORDS
        .stream()
        .map(word -> new Object[]{word})
        .collect(Collectors.toList());
  }

  @Test
  public void testBKTreeSet() {
    assertTrue(tree.contains(word));
  }
}
