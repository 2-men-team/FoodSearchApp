package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.kpi.restaurants.logic.common.algorithms.BKTreeSet;
import ua.kpi.restaurants.logic.common.algorithms.SimilaritySet;
import ua.kpi.restaurants.logic.common.utils.metrics.Levenstein;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class BKTreeSetSimilarityTest {
  public static final String TEST_FILE = "resources/tests/similarity.csv";
  public static final int n = 1000;
  public static final Levenstein metric = new Levenstein();
  public static BKTreeSet tree;

  public final String word;

  public BKTreeSetSimilarityTest(@NotNull String word) {
    this.word = word;
  }

  @BeforeClass
  public static void load() throws IOException {
    tree = new BKTreeSet(new Levenstein());
    tree.addAll(TestHelper.processStopWords(TestHelper.STOP_WORDS));
  }

  @NotNull
  @Parameters
  public static Collection<Object[]> data() throws FileNotFoundException {
    Object[][] tests = new Object[n][1];
    Scanner scanner = new Scanner(new File(TEST_FILE));
    for (int i = 0; i < n && scanner.hasNextLine(); i++) {
      String test = scanner.nextLine().trim();
      tests[i] = new Object[]{ test };
    }

    return Arrays.asList(tests);
  }

  @Test
  public void testBKTreeSetSimilarity() {
    if (tree.contains(word)) assertFalse(tree.add(word));
    else {
      Iterable<String> similar = new ArrayList<>(tree.getSimilarTo(word))
          .stream()
          .map(SimilaritySet.Entry::getElement)
          .collect(Collectors.toList());

      similar.forEach(e -> assertTrue(metric.apply(e, word) <= BKTreeSet.DEFAULT_THRESHOLD));
    }
  }
}
