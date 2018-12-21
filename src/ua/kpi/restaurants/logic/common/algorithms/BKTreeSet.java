package ua.kpi.restaurants.logic.common.algorithms;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.kpi.restaurants.logic.common.utils.metrics.Metric;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/BK-tree">BK-tree</a> data structure.
 *
 * It is a Metric tree that is most commonly used for fuzzy string search. Its is used with metric distances
 * (such as {@link ua.kpi.restaurants.logic.common.utils.metrics.Levenstein} distance) to compute similarity
 * between words and efficiently retrieve information about most similar words to some particular word.
 *
 * It is not the most reasonable choice for fuzzy string search as it does not support queries about the most similar
 * word efficiently (VP-tree might be more suitable). Nevertheless, it is easy to implement and use.
 */
public final class BKTreeSet extends AbstractSet<String> implements SimilaritySet<String>, Serializable {
  private static final long serialVersionUID = 848895510913565910L;

  /** Default threshold for search */
  public static final int DEFAULT_THRESHOLD = 2;

  private Node root;
  private int size = 0;
  private int threshold;
  private final Metric<String, Integer> metric;

  /**
   * Constructs the tree for the specified metric.
   *
   * Delegates the task to {@link #BKTreeSet(Metric, int)}.
   *
   * @param metric - metric to construct tree on (must not be {@code null})
   */
  public BKTreeSet(@NotNull Metric<String, Integer> metric) {
    this(metric, DEFAULT_THRESHOLD);
  }

  /**
   * Constructs the tree for the specified metric and search threshold.
   *
   * @param metric - metric to construct tree on (must not be {@code null})
   * @param threshold - search threshold
   * @throws IllegalArgumentException if threshold id < 0
   */
  public BKTreeSet(@NotNull Metric<String, Integer> metric, int threshold) {
    super();
    this.threshold = validateThreshold(threshold);
    this.metric = metric;
  }

  private static int validateThreshold(int val) {
    if (val < 0) {
      throw new IllegalArgumentException("Invalid threshold value.");
    }

    return val;
  }

  /**
   * Getter for threshold
   *
   * @return current threshold
   */
  public int getThreshold() {
    return threshold;
  }

  /**
   * Setter for threshold
   *
   * @param threshold - new threshold value
   * @throws IllegalArgumentException if threshold id < 0
   */
  public void setThreshold(int threshold) {
    this.threshold = validateThreshold(threshold);
  }

  private static final class Node implements Entry<String>, Serializable {
    private final static long serialVersionUID = -8541287700963696248L;
    private final String word;
    private final SortedMap<Integer, Node> next;
    private int sim;

    private Node(String word) {
      this.word = word;
      this.next = new TreeMap<>();
    }

    @NotNull
    @Override
    public String getElement() {
      return word;
    }

    @Override
    public double getSimilarity() {
      return sim;
    }

    @Override
    public int hashCode() {
      return word.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (o == null) return false;
      if (o == this) return true;
      if (o.getClass() != this.getClass()) return false;
      Node that = (Node) o;
      return that.word.equals(this.word);
    }

    @Override
    public String toString() {
      return String.format("BKTreeSet.Node: %s (%d)", word, sim);
    }
  }

  private static final class BKTreeIterator implements Iterator<String>, Serializable {
    private final static long serialVersionUID = -2314985177087688435L;
    private final Queue<Node> queue;

    private BKTreeIterator(@Nullable Node node) {
      this.queue = new ArrayDeque<>();
      if (node != null) {
        this.queue.add(node);
      }
    }

    @Override
    public boolean hasNext() {
      return !queue.isEmpty();
    }

    @Override
    public String next() {
      if (!hasNext()) {
        throw new NoSuchElementException("Iterator has no elements to iterate.");
      }

      Node node = Objects.requireNonNull(queue.poll(), "Word must not be null");
      queue.addAll(node.next.values());
      return node.word;
    }
  }

  /**
   * Retrieves words and their similarities to the given word
   *
   * @param word - word to compute similarities to (must not be {@code null})
   * @return {@link Set} word-similarity pairs
   */
  @NotNull
  @Override
  public Set<Entry<String>> getSimilarTo(@NotNull String word) {
    Queue<Node> queue = new ArrayDeque<>();
    Set<Entry<String>> result = new HashSet<>();

    queue.add(root);
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      if (node == null) continue;

      int dist = metric.apply(node.word, word);
      if (node.word.charAt(0) == word.charAt(0) && dist <= threshold)
      { node.sim = dist; result.add(node); }
      int low = Math.max(1, dist - threshold), high = dist + threshold + 1;
      queue.addAll(node.next.subMap(low, high).values());
    }

    return result;
  }

  /**
   * Retrieves number of words in this data structure.
   *
   * @return size of this data structure
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Checks whether this data structure is empty.
   *
   * @return {@code true} if empty, {@code false} otherwise
   */
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Checks whether this set contains specified Object.
   *
   * @param o - object to check
   * @return {@code true} if contains, {@code false} otherwise
   * @throws IllegalArgumentException if object is not a {@link String}
   */
  @Override
  public boolean contains(@NotNull Object o) {
    if (!(o instanceof String)) {
      throw new IllegalArgumentException("Argument is not a String.");
    }

    String s = (String) o;
    Node node = root;

    while (node != null) {
      int dist = metric.apply(node.word, s);
      if (dist == 0) return true;
      node = node.next.get(dist);
    }

    return false;
  }

  /**
   * Retrieves {@link Iterator} for this data structure.
   *
   * No particular order is guaranteed.
   *
   * @return retrieved {@link Iterator}
   */
  @Override
  public Iterator<String> iterator() {
    return new BKTreeIterator(root);
  }

  /**
   * Adds element to this data structure.
   *
   * @param s - element to add
   * @return {@code true} if addition was successful, {@code false} otherwise
   */
  @Override
  public boolean add(@NotNull String s) {
    if (root == null) {
      root = new Node(s);
    } else {
      Node node = root;
      while (true) {
        int dist = metric.apply(s, node.word);
        if (dist == 0) {
          return false;
        }

        if (node.next.containsKey(dist)) {
          node = node.next.get(dist);
        } else {
          node.next.put(dist, new Node(s));
          break;
        }
      }
    }

    size++;
    return true;
  }

  /** @deprecated */
  @Deprecated
  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException("Remove is not supported.");
  }

  /** @deprecated */
  @Deprecated
  @Override
  public boolean retainAll(@Nullable Collection<?> collection) {
    throw new UnsupportedOperationException("Retain is not supported.");
  }

  /** @deprecated */
  @Deprecated
  @Override
  public boolean removeAll(@Nullable Collection<?> collection) {
    throw new UnsupportedOperationException("Remove is not supported.");
  }

  /**
   * Deletes all the information from this data structure.
   */
  @Override
  public void clear() {
    root = null;
    size = 0;
  }
}
