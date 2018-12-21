package ua.kpi.restaurants.logic.common.algorithms;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Describes common interface of the family of data structures known as Metric Trees.
 * This interface is aimed at providing a common definition of operations used in this application
 * for similarity retrieval.
 *
 * @param <E> - type of tree elements
 */
public interface SimilaritySet<E> extends Set<E> {
  /**
   * Retrieves {@link Set} of elements, most similar to the current element.
   *
   * Returned {@link Set} is not guaranteed to have any particular properties.
   *
   * @param elem - element under consideration (must not be {@code null})
   * @return {@link Set} of retrieved elements as well as their similarities
   */
  @NotNull Set<Entry<E>> getSimilarTo(@NotNull E elem);

  /**
   * Abstracts over the element and its similarity to some other element.
   *
   * @param <E> - type of element
   */
  interface Entry<E> {
    /**
     * Retrieves current element
     *
     * @return retrieved element
     */
    @NotNull E getElement();

    /**
     * Retrieves similarity between two elements one of which can be obtained using {@link #getElement()}.
     *
     * @return retrieved element
     */
    double getSimilarity();

    /**
     * Produces {@link Comparator} to compare {@code Entry} instances by elements.
     *
     * @param <E> - element type (must be {@link Comparable})
     * @return produced {@link Comparator}
     */
    static <E extends Comparable<? super E>> Comparator<Entry<E>> comparingByElement() {
      return Comparator.comparing(Entry::getElement);
    }

    /**
     * Produces {@link Comparator} to compare {@code Entry} instances by similarity.
     *
     * @param <E> - element type
     * @return produced {@link Comparator}
     */
    static <E> Comparator<Entry<E>> comparingBySimilarity() {
      return Comparator.comparingDouble(Entry::getSimilarity);
    }

    /**
     * Produces {@link Comparator} to compare {@code Entry} instances by elements.
     * It allows to define additional comparator to compare elements.
     *
     * @param comp - additional comparator for elements (must not be {@code null})
     * @param <E> - element type
     * @return produced {@link Comparator}
     */
    static <E> Comparator<Entry<E>> comparingByElement(@NotNull Comparator<? super E> comp) {
      return Comparator.comparing(Entry::getElement, comp);
    }

    /**
     * Produces {@link Comparator} to compare {@code Entry} instances by similarity.
     * It allows to define additional comparator to compare similarities.
     *
     * @param comp - additional comparator for similarities (must not be {@code null})
     * @param <E> - type of elements
     * @return produced {@link Comparator}
     */
    static <E> Comparator<Entry<E>> comparingBySimilarity(@NotNull Comparator<? super Double> comp) {
      return Comparator.comparing(Entry::getSimilarity, comp);
    }
  }
}
