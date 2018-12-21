package ua.kpi.restaurants.logic.common.utils;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;
import org.jetbrains.annotations.NotNull;
import ua.kpi.restaurants.logic.common.exceptions.InvalidQueryException;
import ua.kpi.restaurants.logic.common.exceptions.ProjectRuntimeException;

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

/**
 * Utility class that handles all serialization queries.
 *
 * It provides convenient methods for both native and JSON serialization/deserialization.
 *
 * It uses <a href="http://genson.io/">Genson</a> for fast JSON manipulations.
 */
public final class Serializer {
  private static final Genson GENSON = new GensonBuilder()
      .setFieldFilter(VisibilityFilter.PRIVATE)
      .setConstructorFilter(VisibilityFilter.ALL)
      .useMethods(false)
      .useConstructorWithArguments(true)
      .setThrowExceptionIfNoDebugInfo(true)
      .create();

  private Serializer() {}

  /**
   * Provides convenient way to use Java native serialization algorithm.
   * One of two overloaded methods ({@link #serializeNative(OutputStream, Serializable)} is the other one).
   *
   * @param filename of the file to serialize to (must not be {@code null})
   * @param object to serialize
   * @throws IOException if {@link FileOutputStream} or {@link ObjectOutputStream} throws it
   * @see FileOutputStream#FileOutputStream(String)
   * @see ObjectOutputStream#ObjectOutputStream(OutputStream)
   */
  public static void serializeNative(@NotNull String filename, Serializable object) throws IOException {
    try (OutputStream stream = new FileOutputStream(filename)) {
      serializeNative(stream, object);
    }
  }

  /**
   * Provides convenient way to use Java native serialization algorithm.
   * One of two overloaded methods ({@link #serializeNative(String, Serializable)} is the other one).
   *
   * @param stream to serialize to (must not be {@code null})
   * @param object to serialize
   * @throws IOException if {@link ObjectOutputStream} throws it
   * @see ObjectOutputStream#ObjectOutputStream(OutputStream)
   */
  public static void serializeNative(@NotNull OutputStream stream, Serializable object) throws IOException {
    new ObjectOutputStream(stream).writeObject(object);
  }

  /**
   * Provides convenient way to deserialize an object from a file using Java native serialization.
   * One of two overloaded methods ({@link #deserializeNative(InputStream)} is the other one).
   *
   * @param filename to deserialize an object from
   * @return deserialized object
   * @throws IOException if {@link FileInputStream} or {@link ObjectInputStream} throws it
   * @throws ClassNotFoundException if {@link ObjectInputStream} throws it
   * @see FileInputStream#FileInputStream(String)
   * @see ObjectInputStream#ObjectInputStream(InputStream)
   * @see ObjectInputStream#readObject()
   */
  public static Object deserializeNative(@NotNull String filename) throws IOException, ClassNotFoundException {
    try (InputStream stream = new FileInputStream(filename)) {
      return deserializeNative(stream);
    }
  }

  /**
   * Provides convenient way to deserialize an object from a file using Java native serialization.
   * One of two overloaded methods ({@link #deserializeNative(String)} is the other one).
   *
   * @param stream to deserialize an object from
   * @return deserialized object
   * @throws IOException if {@link ObjectInputStream} throws it
   * @throws ClassNotFoundException if {@link ObjectInputStream} throws it
   * @see ObjectInputStream#ObjectInputStream(InputStream)
   * @see ObjectInputStream#readObject()
   */
  public static Object deserializeNative(@NotNull InputStream stream) throws IOException, ClassNotFoundException {
    return new ObjectInputStream(stream).readObject();
  }

  /**
   * Provides convenient way to serialize given object to JSON format
   *
   * It maintains protocol where the number of bytes in serialized version is sent before the version itself
   *
   * @param stream to serialize to
   * @param object to serialize
   * @throws ProjectRuntimeException if serialization can't be performed due to invalid {@link InputStream}
   * @see DataOutputStream#writeInt(int)
   * @see DataOutputStream#write(byte[])
   * @see DataOutputStream#flush()
   */
  public static void serializeJson(@NotNull OutputStream stream, Object object) {
    DataOutputStream out = new DataOutputStream(stream);
    byte[] json = GENSON.serializeBytes(object);

    try {
      out.writeInt(json.length);
      out.write(json);
      out.flush();
    } catch (IOException e) {
      throw new ProjectRuntimeException("Error while serializing to stream", e);
    }
  }

  /**
   * Provides convenient way to deserialize given object from JSON format
   *
   * Assumption is made that the number of bytes in serialized
   * version of the object is sent before the version itself
   *
   * @param stream to deserialize from
   * @param clazz {@link Class} of deserialized object
   * @param <T> deserialized object's type
   * @return deserialized object
   * @throws InvalidQueryException if assumption does not hold
   * @throws ProjectRuntimeException if serialization failed due to invalid {@link InputStream}
   * @see DataInputStream#readFully(byte[])
   * @see DataInputStream#readInt()
   */
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
      throw new InvalidQueryException("Mismatch between query size and the query itself.", e);
    } catch (IOException e) {
      throw new ProjectRuntimeException("Error while deserializing from stream", e);
    }

    return GENSON.deserialize(json, clazz);
  }
}
