package project.logic.common.utils.preprocessors.mappers;

import org.jetbrains.annotations.NotNull;
import org.tartarus.snowball.ext.PorterStemmer;

public final class Stemmer implements Mapper {
    private final PorterStemmer stemmer = new PorterStemmer();

    @NotNull
    @Override
    public String mapWord(@NotNull String word) {
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
