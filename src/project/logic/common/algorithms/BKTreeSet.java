package project.logic.common.algorithms;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.logic.common.utils.metrics.Metric;

import java.io.Serializable;
import java.util.*;

public final class BKTreeSet extends AbstractSet<String> implements SimilaritySet<String>, Serializable {
    private static final long serialVersionUID = 848895510913565910L;
    public static final int DEFAULT_THRESHOLD = 2;

    private Node root;
    private int size = 0;
    private int threshold;
    private final Metric<String, Integer> metric;

    public BKTreeSet(@NotNull Metric<String, Integer> metric) {
        this(metric, DEFAULT_THRESHOLD);
    }

    public BKTreeSet(@NotNull Metric<String, Integer> metric, int threshold) {
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
            if (node != null) this.queue.add(node);
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

            Node node = Objects.requireNonNull(queue.poll());
            queue.addAll(node.next.values());
            return node.word;
        }
    }

    @NotNull
    @Override
    public Set<Entry<String>> getSimilarTo(@NotNull String s) {
        Queue<Node> queue = new ArrayDeque<>();
        Set<Entry<String>> result = new HashSet<>();

        queue.add(root);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node == null) continue;

            int dist = metric.compute(node.word, s);
            if (node.word.charAt(0) == s.charAt(0) && dist <= threshold)
            { node.sim = dist; result.add(node); }
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
    public boolean contains(@NotNull Object o) {
        String s = (String) o;
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
    public boolean add(@NotNull String s) {
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
