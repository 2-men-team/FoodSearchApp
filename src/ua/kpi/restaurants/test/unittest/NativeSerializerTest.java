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
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class NativeSerializerTest {
  public static final String TEST_INPUT_FILE = "resources/tests/serialize.csv";
  public static final String TEST_OUTPUT_FILE = "resources/tests/deserialize.csv";
  public static final int n = 100;

  public String string;

  public NativeSerializerTest(@NotNull String string) {
    this.string = string;
  }

  @AfterClass
  public static void close() {
    File file = new File(TEST_OUTPUT_FILE);
    file.delete();
  }

  @Parameters
  public static Collection<Object[]> data() throws FileNotFoundException {
    Object[][] tests = new Object[n][1];
    try (Scanner scanner = new Scanner(new File(TEST_INPUT_FILE))) {
      for (int i = 0; i < n && scanner.hasNextLine(); i++) {
        tests[i] = new Object[]{ scanner.nextLine() };
      }
    }

    return Arrays.asList(tests);
  }

  @Test
  public void testNativeSerializer() throws IOException, ClassNotFoundException {
    Serializer.serializeNative(TEST_OUTPUT_FILE, string);
    String actual = (String) Serializer.deserializeNative(TEST_OUTPUT_FILE);
    assertEquals(string, actual);
  }
}
