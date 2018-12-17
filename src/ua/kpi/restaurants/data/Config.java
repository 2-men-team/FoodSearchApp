package ua.kpi.restaurants.data;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.exceptions.InvalidPropertyException;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
  private final Properties properties = new Properties();
  private final Language language;

  private static final class InstanceHolder {
    private static final Config instance = new Config();
  }

  public enum Language {
    RUSSIAN(new RussianProperties()),
    ENGLISH(new EnglishProperties());

    private LanguageProperties properties;

    Language(LanguageProperties properties) {
      this.properties = properties;
    }

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

  public static Config getInstance() {
    return InstanceHolder.instance;
  }

  public Language getLanguage() {
    return language;
  }

  public String getProperty(@NotNull String name) {
    String result = properties.getProperty(name);
    if (result == null) {
      throw new InvalidPropertyException(String.format("'%s' is not defined.", name));
    }

    return result;
  }
}
