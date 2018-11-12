package com.restaurant.search;

import com.restaurant.Config;
import com.restaurant.data.Dish;
import com.restaurant.data.Restaurant;
import com.restaurant.data.Serializer;
import edu.princeton.cs.algs4.MaxPQ;

import java.io.*;
import java.util.*;

public final class ResolveHandler implements Serializable {
    private Map<String, Restaurant> fields;
    private BKTree keySpace;
    private final Denoiser denoiser = new Denoiser(Config.NOISE_PATH);
    private Map<String, Integer> freq = new HashMap<>();

    public ResolveHandler() {
        fields = new HashMap<>();
        keySpace = new BKTree();
        File file = new File(Config.DATA_PATH);

        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(":");
            scanData(scanner);
            scanFreq();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Restaurant[] search(String sentence, double lat, double lon) {
        return search(sentence, new Point(lat, lon));
    }

    public Restaurant[] search(String sentence, Point that) {
        String[] query = extractKeys(sentence);
        Restaurant[] restaurants = search(query);

        Arrays.sort(restaurants, (Restaurant a, Restaurant b) ->
                (Double.compare(a.distanceTo(that), b.distanceTo(that))));

        return restaurants;
    }

    private Restaurant[] search(String[] query) {
        Comparator<Restaurant> comp = (Restaurant a, Restaurant b) -> (Integer.compare(a.rank(), b.rank()));
        MaxPQ<Restaurant> pq = new MaxPQ<>(Config.PQ_SIZE, comp);

        for (Map.Entry entry : fields.entrySet()) {
            Restaurant restaurant = (Restaurant) entry.getValue();
            restaurant.rank(query);
            if (restaurant.rank() != 0) pq.insert(restaurant);
        }

        Restaurant[] restaurants = new Restaurant[Config.SIZE];
        for (int i = 0; i < Config.SIZE; i++) restaurants[i] = pq.delMax();
        return restaurants;
    }

    private String[] extractKeys(String sentence) {
        String[] query = denoiser.denoise(sentence);
        List<String> q = new ArrayList<>();

        for (String key : query) {
            if (keySpace.contains(key)) {
                q.add(key);
            } else {
                MaxPQ<String> pq = new MaxPQ<>((String a, String b) -> (freq.get(a) - freq.get(b)));

                for (String s : keySpace.similar(key)) {
                    System.out.println(s);
                    if (freq.containsKey(s)) pq.insert(s);
                }
                if (!pq.isEmpty()) q.add(pq.delMax());
            }
        }
        return q.toArray(new String[0]);
    }

    private void scanFreq() {
        File file = new File(Config.FREQ_PATH);

        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(":");

            while (scanner.hasNextLine()) {
                String word = scanner.next().toLowerCase().trim();
                int frequency = Integer.parseInt(scanner.next().trim());
                freq.put(word, frequency);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void scanData(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String name = scanner.next().trim().toLowerCase();
            String location = scanner.next().trim().toLowerCase();
            String dishName = "";
            double price, longitude, latitude;

            try {
                latitude = Double.parseDouble(scanner.next().trim().toLowerCase());
                longitude = Double.parseDouble(scanner.next().trim().toLowerCase());
                dishName = String.join(" ", denoiser.denoise(scanner.next().trim().toLowerCase()));
                price = Double.parseDouble(scanner.next().trim().toLowerCase());
            } catch (NumberFormatException e) {
                latitude = 40.7;
                longitude = -73.8;
                price = 0;
            }

            Dish dish = new Dish(dishName, price);
            if (fields.containsKey(name))
                fields.get(name).getDishes().add(dish);
            else
                fields.put(name, new Restaurant(name, location, latitude, longitude, new Dish[]{dish}));
            keySpace.add(name);
            keySpace.add(location);
            keySpace.addAll(Arrays.asList(denoiser.preprocess(dishName)));
        }
    }

    public static void main(String[] args) throws IOException {
        //ResolveHandler resolver = new ResolveHandler();

        System.out.println("Loading database...");
        ResolveHandler resolver = Serializer.deserialize(Config.SYSTEM_PATH);
        System.out.println("Loaded database.");
        //Serializer.serialize(resolver);

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.println("> ");
            String query = reader.readLine();
            Restaurant[] restaurants = resolver.search(query, 41, -73);
            Serializer.printRestaurants(restaurants);
        }
    }
}
