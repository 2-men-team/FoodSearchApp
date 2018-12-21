package ua.kpi.restaurants.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.PorterStemmer;
import org.tartarus.snowball.ext.RussianStemmer;

import java.util.function.UnaryOperator;

/**
 * Simple implementation of stemming algorithm.
 *
 * It uses <a hreaf="https://lucene.apache.org/core/4_1_0/analyzers-common/org/tartarus/snowball/ext/PorterStemmer.html">Porter Stemmer</a>
 * algorithm for English
 * and <a href="https://lucene.apache.org/core/4_1_0/analyzers-common/org/tartarus/snowball/ext/RussianStemmer.html">Russian Stemmer</a> for Russian.
 */
public final class Stemmer implements UnaryOperator<String> {
  /** English stemmer */
  public static final Stemmer ENGLISH = new Stemmer(new PorterStemmer());

  /** Russian stemmer */
  public static final Stemmer RUSSIAN = new Stemmer(new RussianStemmer());

  private final SnowballProgram stemmer;

  private Stemmer(SnowballProgram stemmer) {
    this.stemmer = stemmer;
  }

  /**
   * Executes the algorithm
   *
   * @param word to stem (must not be {@code null})
   * @return stemmed word
   */
  @NotNull
  @Override
  public String apply(@NotNull String word) {
    stemmer.setCurrent(word);
    stemmer.stem();
    return stemmer.getCurrent();
  }
}
