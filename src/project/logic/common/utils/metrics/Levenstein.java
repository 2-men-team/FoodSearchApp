package project.logic.common.utils.metrics;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Levenstein implements WordMetric {
    private final static long serialVersionUID = -2991852047178025201L;

    @Override
    public int compute(@NotNull String a, @NotNull String b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        int n = a.length(), m = b.length();

        if (n == 0) return m;
        if (m == 0) return n;

        int[] buffer = new int[n + 1]; // buffer[0] is never used
        for (int i = 1; i <= n; i++) buffer[i] = i;

        for (int i = 1; i <= m; i++) {
            int left = i, diagonal = i - 1;

            for (int j = 1; j <= n; j++) {
                int up = buffer[j];
                int cost = b.charAt(i - 1) != a.charAt(j - 1) ? 1 : 0;
                buffer[j] = Math.min(up + 1, Math.min(left + 1, diagonal + cost));
                left = buffer[j];
                diagonal = up;
            }
        }

        return buffer[n];
    }
}
