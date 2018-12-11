package project.logic.strategies.preprocessing.routines;

import org.jetbrains.annotations.NotNull;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.PorterStemmer;
import org.tartarus.snowball.ext.RussianStemmer;

import java.util.function.Function;

public final class Stemmer implements Function<String, String> {
    public static final Stemmer ENGLISH = new Stemmer(new PorterStemmer());
    public static final Stemmer RUSSIAN = new Stemmer(new RussianStemmer());

    private final SnowballProgram stemmer;

    private Stemmer(SnowballProgram stemmer) {
        this.stemmer = stemmer;
    }

    @NotNull
    @Override
    public String apply(@NotNull String word) {
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
