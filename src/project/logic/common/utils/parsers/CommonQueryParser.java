package project.logic.common.utils.parsers;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import project.logic.common.utils.preprocessors.denoiser.Denoiser;
import project.logic.common.utils.preprocessors.mappers.Mapper;
import project.logic.common.utils.preprocessors.mappers.SpellCorrector;
import project.logic.common.utils.preprocessors.mappers.Stemmer;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class CommonQueryParser implements QueryParser {
    public static final CommonQueryParser DUMMY = new CommonQueryParser.Builder().build();
    @Language("RegExp") public static final String DELIMITERS = "(\\s|\\d|\\p{Punct})+";

    private final Pattern pattern;
    private final Denoiser denoiser;
    private final Function<String, String> mapper;

    private CommonQueryParser(String delimiters, Denoiser denoiser, Function<String, String> mapper) {
        this.denoiser = denoiser;
        this.mapper = mapper;
        this.pattern = Pattern.compile(delimiters);
    }

    @Override
    @NotNull
    public Stream<String> parse(@NotNull String query) {
        return pattern.splitAsStream(query)
                .filter(word -> !denoiser.isNoise(word))
                .map(mapper);
    }

    public static final class Builder {
        private String delimiters = DELIMITERS;
        private Mapper spellCorrector = Mapper.DUMMY;
        private Mapper stemmer = Mapper.DUMMY;
        private Denoiser denoiser = Denoiser.DUMMY;

        public Builder setDelimiters(@NotNull String delimiters) {
            this.delimiters = delimiters;
            return this;
        }

        public Builder setSpellCorrector(@NotNull Mapper spellCorrector) {
            this.spellCorrector = spellCorrector;
            return this;
        }

        public Builder setStemmer(@NotNull Mapper stemmer) {
            this.stemmer = stemmer;
            return this;
        }

        public Builder setDenoiser(@NotNull Denoiser denoiser) {
            this.denoiser = denoiser;
            return this;
        }

        public CommonQueryParser build() {
            Function<String, String> mapper = stemmer::mapWord;
            mapper = mapper.andThen(spellCorrector::mapWord);
            return new CommonQueryParser(delimiters, denoiser, mapper);
        }
    }
}
