package com.restaurant.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public final class Denoiser implements Serializable {
    private HashSet<String> noise;
    private static final String PUNCTUATION = "',./!@#$^&;?|*()-+";

    public Denoiser(String path) {
        noise = new HashSet<>();
        File file = new File(path);

        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",");

            while (scanner.hasNext()) {
                String word = scanner.next().trim();
                noise.add(word.toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String[] denoise(String query) {
        String[] keys = preprocess(query);
        List<String> q = new ArrayList<>();

        for (String key : keys)
            if (!noise.contains(key)) q.add(key);
        return q.toArray(new String[0]);
    }

    public String[] preprocess(String query) {
        query = query.toLowerCase();
        for (String ch : PUNCTUATION.split(""))
            query = query.replace(ch, "");
        return query.split(" ");
    }
}
