package ua.kpi.restaurants.logic.common.utils;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;
import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.exceptions.InvalidQueryException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public final class Serializer {
  private static final Genson genson = new GensonBuilder()
      .setFieldFilter(VisibilityFilter.PRIVATE)
      .setConstructorFilter(VisibilityFilter.ALL)
      .useMethods(false)
      .useConstructorWithArguments(true)
      .setThrowExceptionIfNoDebugInfo(true)
      .create();

  private Serializer() { }

  public static void serializeNative(@NotNull String filename, Serializable object) throws IOException {
    try (OutputStream stream = new FileOutputStream(filename)) {
      serializeNative(stream, object);
    }
  }

  public static void serializeNative(@NotNull OutputStream stream, Serializable object) throws IOException {
    new ObjectOutputStream(stream).writeObject(object);
  }

  public static Object deserializeNative(@NotNull String filename) throws IOException, ClassNotFoundException {
    try (InputStream stream = new FileInputStream(filename)) {
      return deserializeNative(stream);
    }
  }

  public static Object deserializeNative(@NotNull InputStream stream) throws IOException, ClassNotFoundException {
    return new ObjectInputStream(stream).readObject();
  }

  public static void serializeJson(@NotNull OutputStream stream, Object object) {
    DataOutputStream out = new DataOutputStream(stream);
    byte[] json = genson.serializeBytes(object);

    try {
      out.writeInt(json.length);
      out.write(json);
      out.flush();
    } catch (IOException e) {
      throw new RuntimeException("Error while serializing to stream", e);
    }
  }

  public static <T> T deserializeJson(@NotNull InputStream stream, @NotNull Class<T> clazz) {
    DataInputStream in = new DataInputStream(stream);
    byte[] json;

    try {
      int size = in.readInt();
      if (size <= 0) {
        throw new InvalidQueryException("Size of the message sent is <= 0.");
      }

      json = new byte[size];
      in.readFully(json);
    } catch (EOFException e) {
      throw new InvalidQueryException("Query format mismatch.", e);
    } catch (IOException e) {
      throw new RuntimeException("Error while deserializing from stream", e);
    }

    return genson.deserialize(json, clazz);
  }
}
