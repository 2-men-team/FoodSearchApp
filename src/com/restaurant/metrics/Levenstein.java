package com.restaurant.metrics;

import java.io.Serializable;

public final class Levenstein implements Metric, Serializable {
    @Override
    public int compute(String a, String b) {
        final int length = a.length();
        final int[] buffer = new int[length + 1];
        final int n = b.length();

        for (int i = 0; i <= length; i++) buffer[i] = i;

        for (int i = 1; i <= n; i++) {
            int left = i, diagonal = i - 1;

            for (int j = 1; j <= length; j++) {
                int up = buffer[j];
                int cost = b.charAt(i - 1) != a.charAt(j - 1) ? 1 : 0;
                buffer[j] = Math.min(up + 1, Math.min(left + 1, diagonal + cost));
                left = buffer[j];
                diagonal = up;
            }
        }
        return buffer[length];
    }
}
