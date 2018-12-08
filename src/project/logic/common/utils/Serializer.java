package project.logic.common.utils;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.reflect.VisibilityFilter;
import org.jetbrains.annotations.NotNull;

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

    public static String serializeJson(Object object) {
        return genson.serialize(object);
    }

    public static void serializeJson(@NotNull OutputStream stream, Object object) {
        genson.serialize(object, stream);
    }

    public static <T> T deserializeJson(@NotNull InputStream stream, @NotNull Class<T> clazz) {
        return genson.deserialize(stream, clazz);
    }

    public static <T> T deserializeJson(@NotNull String json, @NotNull Class<T> clazz) {
        return genson.deserialize(json, clazz);
    }
}
