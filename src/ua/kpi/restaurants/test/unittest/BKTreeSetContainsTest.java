package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.kpi.restaurants.logic.common.algorithms.BKTreeSet;
import ua.kpi.restaurants.logic.common.utils.metrics.Levenstein;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BKTreeSetContainsTest {
  public static BKTreeSet tree;

  public final String word;

  public BKTreeSetContainsTest(@NotNull String word) {
    this.word = word;
  }

  @BeforeClass
  public static void load() throws IOException {
    tree = new BKTreeSet(new Levenstein());
    tree.addAll(TestHelper.processStopWords(TestHelper.STOP_WORDS));
  }

  @NotNull
  @Parameters
  public static Iterable<Object> data() throws IOException {
    List<String> words = new ArrayList<>(TestHelper.processStopWords(TestHelper.STOP_WORDS));
    Object[][] tests = new Object[words.size()][1];

    for (int i = 0; i < words.size(); i++) {
      tests[i] = new Object[]{words.get(i)};
    }

    return Arrays.asList(tests);
  }

  @Test
  public void testBKTreeSet() {
    assertTrue(tree.contains(word));
  }
}
