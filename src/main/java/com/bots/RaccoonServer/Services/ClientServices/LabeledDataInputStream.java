package com.bots.RaccoonServer.Services.ClientServices;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.InputStream;

public class LabeledDataInputStream extends DataInputStream {
    public final int id;

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     * @param id
     */
    public LabeledDataInputStream(@NotNull InputStream in, int id) {
        super(in);
        this.id = id;
    }
}
