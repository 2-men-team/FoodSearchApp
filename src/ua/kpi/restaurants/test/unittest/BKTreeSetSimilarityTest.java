package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.kpi.restaurants.logic.common.algorithms.BKTreeSet;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;
import ua.kpi.restaurants.logic.common.utils.metrics.Levenstein;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class BKTreeSetSimilarityTest {
  private static final String TEST_FILE = "resources/tests/similarity.csv";
  private static final Levenstein METRIC = new Levenstein();
  private static final Set<String> WORDS;
  private static BKTreeSet tree;

  private final String word;

  static {
    try {
      WORDS = TestHelper.processStopWords(TestHelper.STOP_WORDS);
    } catch (IOException e) {
      throw new ProjectRuntimeException("Error while initializing test cases", e);
    }
  }

  public BKTreeSetSimilarityTest(@NotNull String word) {
    this.word = word;
  }

  @BeforeClass
  public static void load() {
    tree = new BKTreeSet(new Levenstein());
    tree.addAll(WORDS);
  }

  @NotNull
  @Parameters
  public static Collection<Object[]> data() throws FileNotFoundException {
    List<Object[]> tests = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(TEST_FILE))) {
      while (scanner.hasNextLine()) {
        Object[] entry = new Object[]{scanner.nextLine()};
        tests.add(entry);
      }
    }

    return tests;
  }

  @Test
  public void testBKTreeSetSimilarity() {
    if (tree.contains(word)) {
      assertFalse(tree.add(word));
    } else {
      tree.getSimilarTo(word)
          .stream()
          .map(SimilaritySet.Entry::getElement)
          .forEach(e -> assertTrue(METRIC.apply(e, word) <= BKTreeSet.DEFAULT_THRESHOLD));
    }
  }
}
