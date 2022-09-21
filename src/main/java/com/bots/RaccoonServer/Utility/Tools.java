package com.bots.RaccoonServer.Utility;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Tools {
    public static String getChecksumForObject(Serializable object) throws IOException, NoSuchAlgorithmException {
        try (ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(byteOutStream)) {
            oos.writeObject(object);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(byteOutStream.toByteArray());
            return DatatypeConverter.printHexBinary(digest);
        }
    }
}
