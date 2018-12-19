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

@RunWith(Parameterized.class)
public class NativeSerializerTest {
  private static final String TEST_INPUT_FILE = "resources/tests/serialize.csv";
  private static final String TEST_OUTPUT_FILE = "resources/tests/deserialize.csv";

  private final String string;

  public NativeSerializerTest(@NotNull String string) {
    this.string = string;
  }

  @AfterClass
  public static void close() {
    File file = new File(TEST_OUTPUT_FILE);
    file.delete();
  }

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

  @Test
  public void testNativeSerializer() throws IOException, ClassNotFoundException {
    Serializer.serializeNative(TEST_OUTPUT_FILE, string);
    String actual = (String) Serializer.deserializeNative(TEST_OUTPUT_FILE);
    assertEquals(string, actual);
  }
}
