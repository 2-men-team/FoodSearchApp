package project.logic.common.algorithms;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface SimilaritySet<E> extends Set<E> {
    @NotNull Iterable<Entry<E>> getSimilarTo(@NotNull E elem);
    @NotNull default Spliterator<Entry<E>> spliterator(@NotNull E elem) {
        return getSimilarTo(elem).spliterator();
    }

    @NotNull default Stream<Entry<E>> stream(@NotNull E elem) {
        return StreamSupport.stream(spliterator(elem), false);
    }

    @NotNull default Stream<Entry<E>> parallelStream(@NotNull E elem) {
        return StreamSupport.stream(spliterator(elem), true);
    }

    interface Entry<E> {
        @NotNull E getElement();
        int getSimilarity();

        int hashCode();
        boolean equals(Object o);

        static <E extends Comparable<? super E>> Comparator<Entry<E>> comparingByElement() {
            return Comparator.comparing(Entry::getElement);
        }

        static <E> Comparator<Entry<E>> comparingBySimilarity() {
            return Comparator.comparingInt(Entry::getSimilarity);
        }

        static <E> Comparator<Entry<E>> comparingByElement(@NotNull Comparator<? super E> comp) {
            return Comparator.comparing(Entry::getElement, Objects.requireNonNull(comp));
        }

        static <E> Comparator<Entry<E>> comparingBySimilarity(@NotNull Comparator<? super Integer> comp) {
            return Comparator.comparing(Entry::getSimilarity, Objects.requireNonNull(comp));
        }
    }
}
