package project.logic.common.algorithms;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.common.utils.metrics.WordMetric;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

public final class BKTreeSet extends AbstractSet<String> implements SimilaritySet<String>, Serializable {
    private static final long serialVersionUID = 7209147984236242755L;
    public static final int DEFAULT_THRESHOLD = 2;

    private Node root;
    private int size = 0;
    private int threshold;
    private final WordMetric metric;

    public BKTreeSet(@NotNull WordMetric metric) {
        this(metric, DEFAULT_THRESHOLD);
    }

    public BKTreeSet(@NotNull WordMetric metric, int threshold) {
        super();
        this.threshold = validateThreshold(threshold);
        this.metric = metric;
    }

    private static int validateThreshold(int val) {
        if (val < 0) throw new IllegalArgumentException("Invalid threshold value.");
        return val;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = validateThreshold(threshold);
    }

    private static final class Node implements Serializable {
        private static final long serialVersionUID = 652665808894847181L;

        private String word;
        private SortedMap<Integer, Node> next;

        private Node(String word) {
            this.word = word;
            this.next = new TreeMap<>();
        }
    }

    public static final class Entry implements SimilaritySet.Entry<String>, Serializable {
        private final static long serialVersionUID = 478885803938387288L;
        private String word;
        private int sim;

        private Entry(@NotNull String word, int sim) {
            this.word = word;
            this.sim = sim;
        }

        @NotNull
        @Override
        public String getElement() {
            return word;
        }

        @Override
        public int getSimilarity() {
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
            Entry that = (Entry) o;
            return that.word.equals(this.word);
        }

        @Override
        public String toString() {
            return String.format("BKTreeSet.Entry: %s (%d)", word, sim);
        }
    }

    private static final class BKTreeIterator implements Iterator<String>, Serializable {
        private final static long serialVersionUID = -2314985177087688435L;
        private Queue<Node> queue;

        private BKTreeIterator(@Nullable Node node) {
            this.queue = new ArrayDeque<>();
            if (node != null) this.queue.add(node);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public String next() {
            Node node = Objects.requireNonNull(queue.poll());
            queue.addAll(node.next.values());
            return node.word;
        }
    }

    @NotNull
    @Override
    public Iterable<SimilaritySet.Entry<String>> getSimilarTo(@NotNull String s) {
        Objects.requireNonNull(s);
        Queue<Node> queue = new ArrayDeque<>();
        Queue<SimilaritySet.Entry<String>> result = new ArrayDeque<>();

        queue.add(root);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node == null) continue;

            int dist = metric.compute(node.word, s);
            if (node.word.charAt(0) == s.charAt(0) && dist <= threshold)
                result.add(new Entry(node.word, dist));
            int low = Math.max(1, dist - threshold), high = dist + threshold + 1;
            queue.addAll(node.next.subMap(low, high).values());
        }

        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        String s = (String) Objects.requireNonNull(o);
        Node node = root;

        while (node != null) {
            int dist = metric.compute(node.word, s);
            if (dist == 0) return true;
            node = node.next.get(dist);
        }

        return false;
    }

    @Override
    public Iterator<String> iterator() {
        return new BKTreeIterator(root);
    }

    @Override
    public boolean add(String s) {
        Objects.requireNonNull(s);

        if (root == null) root = new Node(s);
        else {
            Node node = root;
            while (true) {
                int dist = metric.compute(s, node.word);
                if (dist == 0) return false;
                if (node.next.containsKey(dist)) node = node.next.get(dist);
                else { node.next.put(dist, new Node(s)); break; }
            }
        }

        size++;
        return true;
    }

    @Deprecated
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Remove is not supported.");
    }

    @Deprecated
    @Override
    public boolean retainAll(@Nullable Collection<?> collection) {
        throw new UnsupportedOperationException("Retain is not supported.");
    }

    @Deprecated
    @Override
    public boolean removeAll(@Nullable Collection<?> collection) {
        throw new UnsupportedOperationException("Remove is not supported.");
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }
}
