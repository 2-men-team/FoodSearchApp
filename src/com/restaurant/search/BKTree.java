package com.restaurant.search;

import com.restaurant.Config;
import com.restaurant.metrics.Levenstein;
import com.restaurant.metrics.Metric;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public final class BKTree implements Serializable {
    private Node root;
    private int TOLERANCE = Config.TOLERANCE;
    private final Metric metric;

    public BKTree() {
        this.root = new Node("");
        this.metric = new Levenstein();
    }

    public BKTree(Metric metric) {
        this.root = new Node("");
        this.metric = metric;
    }

    public BKTree(String word) {
        this.root = new Node(word);
        this.metric = new Levenstein();
    }

    private static final class Node implements Serializable {
        String word = "";
        Map<Integer, Node> children;
        int distance;

        Node(String word) {
            this.word = word;
            this.children = new TreeMap<>();
            this.distance = -1;
        }

        Node(String word, int distance) {
            this.word = word;
            this.distance = distance;
            this.children = new TreeMap<>();
        }
    }

    public void addAll(Iterable<String> collection) {
        for (String key : collection) add(key);
    }

    public boolean contains(String word) {
        if (root.word.equals("")) return false;
        return contains(root, word);
    }

    public void add(String word) {
        if (root.word.equals(""))
            root.word = word;
        else add(root, word);
    }

    public ArrayList<String> similar(String word) {
        ArrayList<String> list = new ArrayList<>();

        if (root.word.equals("")) return list;
        else return similar(root, word, list);
    }

    private boolean contains(Node current, String word) {
        if (current == null || current.word.equals("")) return false;

        int distance = metric.compute(current.word, word);
        if (distance == 0) return true;

        if (!current.children.containsKey(distance)) return false;
        else return contains(current.children.get(distance), word);
    }

    private ArrayList<String> similar(Node current, String word, ArrayList<String> list) {
        if (current == null) return list;
        if (current.word.equals("")) return list;

        int distance = metric.compute(current.word, word);
        if (distance <= TOLERANCE) list.add(current.word);

        int min = (distance - TOLERANCE < 0) ? 1 : (distance - TOLERANCE);

        while (min < distance + TOLERANCE) {
            similar(current.children.get(min), word, list);
            min++;
        }
        return list;
    }

    private void add(Node current, String word) {
        int distance = metric.compute(current.word, word);

        if (!current.children.containsKey(distance))
            current.children.put(distance, new Node(word, distance));
        else {
            if (current.children.get(distance).word.equals(word)) return;
            add(current.children.get(distance), word);
        }
    }

    private static int cost(char a, char b) {
        return a == b ? 0 : 1;
    }

    private static int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    public static void main(String[] args) {
        String[] dictionary = new String[]{"hell","help","shel","smell", "fell","felt","oops","pop","oouch","halt"};

        BKTree tree = new BKTree();

        for (String word : dictionary)
            tree.add(word);

        // check similarity search
        String similar = "hell";
        ArrayList<String> s = tree.similar(similar);

        for (String word : s)
            System.out.println(word);

        // check contains method
        for (String word : dictionary)
            System.out.println(word + " " + tree.contains(word));

        String[] not = new String[]{"contains", "hells", "Jeff", "Dean"};

        for (String word : not)
            System.out.println(word + " " + tree.contains(word));
    }
}
