package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.exceptions.InvalidPropertyException;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Contains major app configurations.
 *
 * Its purpose is to provide application with convenient interface to preloaded configurations.
 * It is a {@code Singleton}.
 *
 * Configurations are specified in separate files. Those files must be set via setting
 * {@code ua.kpi.restaurants.data.Config.properties} property during application startup.
 *
 * Every such a file must have the following properties defined:
 * <ul>
 *   <li>{@code ua.kpi.restaurants.data.Config.language} - the current language (see {@link Language})</li>
 *   <li>{@code ua.kpi.restaurants.data.Config.stopWords} - path to file containing stop words (system dependent)</li>
 *   <li>{@code ua.kpi.restaurants.data.Config.dataSet} - path to file containing the data set itself (system dependent)</li>
 *   <li>{@code ua.kpi.restaurants.data.Config.dataBase} - path to file data base should be serialized to and deserialized from</li>
 *   <li>{@code ua.kpi.restaurants.data.Config.dataSet.delimiter} - delimiters separating data base fields (as regexp)</li>
 * </ul>
 *
 * Example of configuration file:
 * <pre>
 *   <code>
 *     ua.kpi.restaurants.data.Config.language = ENGLISH
 *     ua.kpi.restaurants.data.Config.stopWords = resources/stop_words_en.txt
 *     ua.kpi.restaurants.data.Config.dataSet = resources/data.csv
 *     ua.kpi.restaurants.data.Config.dataBase = resources/db_en.ser
 *     ua.kpi.restaurants.data.Config.dataSet.delimiter = \\|
 *   </code>
 * </pre>
 *
 * Stop words file is a plain text with words separated by whitespaces.
 * Data set file is a plain <a href="https://en.wikipedia.org/wiki/Delimiter-separated_values">delimiter-separated file</a>
 * with delimiter specified by {@code ua.kpi.restaurants.data.Config.dataSet.delimiter} property.
 *
 * @see Language
 */
public final class Config {
  private final Properties properties = new Properties();
  private final Language language;

  private static final class InstanceHolder {
    private static final Config instance = new Config();
  }

  /**
   * Provides access to all available languages.
   * Only Russian and English are supported.
   *
   * @see LanguageProperties
   * @see RussianProperties
   * @see EnglishProperties
   */
  public enum Language {
    /** Specifies properties for Russian language. */
    RUSSIAN(new RussianProperties()),

    /** Specifies properties for English language. */
    ENGLISH(new EnglishProperties());

    private final LanguageProperties properties;

    Language(LanguageProperties properties) {
      this.properties = properties;
    }

    /**
     * Provides access to {@link LanguageProperties} instance for current language.
     *
     * @return instance of {@link LanguageProperties}
     */
    public LanguageProperties getProperties() {
      return properties;
    }
  }

  private Config() {
    String filename = System.getProperty("ua.kpi.restaurants.data.Config.properties");
    if (filename == null) {
      throw new InvalidPropertyException("'ua.kpi.restaurants.data.Config.properties' is not defined.");
    }

    try (InputStream stream = new FileInputStream(filename)) {
      properties.load(stream);
    } catch (FileNotFoundException e) {
      throw new InvalidPropertyException("'ua.kpi.restaurants.data.Config.properties' is invalid", e);
    } catch (IOException e) {
      throw new ProjectRuntimeException(String.format("Exception while reading from '%s'", filename), e);
    }

    String lang = properties.getProperty("ua.kpi.restaurants.data.Config.language");
    if (lang == null) {
      throw new InvalidPropertyException("'ua.kpi.restaurants.data.Config.language' is not defined.");
    }

    try {
      language = Language.valueOf(lang);
    } catch (IllegalArgumentException e) {
      throw new InvalidPropertyException("'ua.kpi.restaurants.data.Config.language' is invalid", e);
    }
  }

  /**
   * Initializes on the first call and provides access to Config's instance.
   * @return {@code Config} instance
   * @throws InvalidPropertyException if any of the properties specified earlier either undefined or invalid
   * @throws ProjectRuntimeException if it is not possible to read properties from file specialized by {@code ua.kpi.restaurants.data.Config.properties} property
   */
  public static Config getInstance() {
    return InstanceHolder.instance;
  }

  /**
   * Provides access to the language currently in use.
   * @return instance of {@link Language} enum
   */
  public Language getLanguage() {
    return language;
  }

  /**
   * Provides access to some property by its name.
   * @param name property name (must not be {@code null})
   * @return property value for specified key
   * @throws InvalidPropertyException if property does not exist
   */
  public String getProperty(@NotNull String name) {
    String result = properties.getProperty(name);
    if (result == null) {
      throw new InvalidPropertyException(String.format("'%s' is not defined.", name));
    }

    return result;
  }
}
