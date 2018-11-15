package project.logic.common.utils;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public final class Serializer {
    private Serializer() { }

    public static void serialize(@NotNull String filename, Serializable object) throws IOException {
        try (OutputStream stream = new FileOutputStream(filename)) {
            serialize(stream, object);
        }
    }

    public static void serialize(@NotNull OutputStream stream, Serializable object) throws IOException {
        try (ObjectOutput output = new ObjectOutputStream(stream)) {
            output.writeObject(object);
        }
    }

    public static Object deserialize(@NotNull String filename) throws IOException, ClassNotFoundException {
        try (InputStream stream = new FileInputStream(filename)) {
            return deserialize(stream);
        }
    }

    public static Object deserialize(@NotNull InputStream stream) throws IOException, ClassNotFoundException {
        try (ObjectInput input = new ObjectInputStream(stream)) {
            return input.readObject();
        }
    }
}
