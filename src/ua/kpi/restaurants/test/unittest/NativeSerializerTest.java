package ua.kpi.restaurants.test.unittest;

import org.jetbrains.annotations.NotNull;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.kpi.restaurants.logic.common.utils.Serializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;

/**
 * The {@code NativeSerializerTest} represents test class.
 * It tests following methods of {@link Serializer} class.
 *
 * <ul>
 *   <li>{@code serializeNative}</li>
 *   <li>{@code deserializeNative}</li>
 * </ul>
 */
@RunWith(Parameterized.class)
public class NativeSerializerTest {
  private static final String TEST_INPUT_FILE = "resources/tests/serialize.csv";
  private static final String TEST_OUTPUT_FILE = "resources/tests/deserialize.csv";

  private final String string;

  /**
   * Initialize test parameter.
   *
   * @param string test string
   */
  public NativeSerializerTest(@NotNull String string) {
    this.string = string;
  }

  /**
   * Deletes {@code TEST_OUTPUT_FILE} helper file.
   */
  @AfterClass
  public static void close() {
    File file = new File(TEST_OUTPUT_FILE);
    file.delete();
  }

  /**
   * Loads testing data.
   *
   * @return collection {@code Collection<Object[]>} of objects
   *         for testing
   * @throws FileNotFoundException if {@code TEST_INPUT_FILE} is missing
   */
  @NotNull
  @Parameters
  public static Collection<Object[]> data() throws FileNotFoundException {
    List<Object[]> tests = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(TEST_INPUT_FILE))) {
      while (scanner.hasNextLine()) {
        tests.add(new Object[]{ scanner.nextLine() });
      }
    }

    return tests;
  }

  /**
   * Tests {@link Serializer} {@code serializeNative}
   * and {@code deserializeNative} methods.
   *
   * @throws IOException if {@code TEST_OUTPUT_FILE} is missing
   * @throws ClassNotFoundException if deserialized class is not present
   */
  @Test
  public void testNativeSerializer() throws IOException, ClassNotFoundException {
    Serializer.serializeNative(TEST_OUTPUT_FILE, string);
    String actual = (String) Serializer.deserializeNative(TEST_OUTPUT_FILE);
    assertEquals(string, actual);
  }
}
