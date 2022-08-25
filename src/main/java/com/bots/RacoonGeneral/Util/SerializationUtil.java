package com.bots.RacoonGeneral.Util;

import java.io.*;
import java.util.Base64;

public abstract class SerializationUtil {
    /**
     * Read the object from Base64 string.
     */
    public static Object fromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        Object out = objectInputStream.readObject();
        objectInputStream.close();
        return out;
    }

    /**
     * Write the object to a Base64 string.
     */
    public static String toString(Serializable in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(in);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
}
