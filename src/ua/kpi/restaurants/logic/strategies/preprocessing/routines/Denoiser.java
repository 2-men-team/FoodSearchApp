package ua.kpi.restaurants.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface Denoiser extends UnaryOperator<List<String>> {
  Denoiser DUMMY = new Denoiser() {
    @Override
    public boolean isNoise(@NotNull String word) {
      return false;
    }

    @NotNull
    @Override
    public Stream<String> asStream(@NotNull List<String> query) {
      return query.stream();
    }

    @NotNull
    @Override
    public List<String> apply(@NotNull List<String> query) {
      return query;
    }
  };

  boolean isNoise(@NotNull String word);

  @NotNull Stream<String> asStream(@NotNull List<String> query);

  @NotNull List<String> apply(@NotNull List<String> query);
}
