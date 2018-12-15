package ua.kpi.restaurants;

import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.data.EnglishProperties;
import ua.kpi.restaurants.data.LanguageProperties;
import ua.kpi.restaurants.data.RussianProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
  private final Properties properties = new Properties();
  private final LanguageProperties languageProperties;

  private static final class InstanceHolder {
    private static final Config instance = new Config();
  }

  private Config() {
    String filename = System.getProperty("ua.kpi.restaurants.Config.properties");

    try (InputStream stream = new FileInputStream(filename)) {
      properties.load(stream);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid property: 'ua.kpi.restaurants.Config.properties'", e);
    } catch (IOException e) {
      throw new RuntimeException(String.format("Exception while reading from '%s'", filename), e);
    }

    String language = properties.getProperty("ua.kpi.restaurants.Config.language");
    if (language == null) {
      throw new IllegalArgumentException("Language is not defined.");
    }

    switch (language) {
      case "eng":
        languageProperties = new EnglishProperties();
        break;
      case "rus":
        languageProperties = new RussianProperties();
        break;
      default:
        throw new IllegalArgumentException("Invalid property: 'ua.kpi.restaurants.Config.language'");
    }
  }

  public static Config getInstance() {
    return InstanceHolder.instance;
  }

  public LanguageProperties getLanguageProperties() {
    return languageProperties;
  }

  public String getProperty(@NotNull String name) {
    String result = properties.getProperty(name);
    if (result == null) {
      throw new IllegalArgumentException(String.format("Property '%s' is not defined.", name));
    }

    return result;
  }
}
