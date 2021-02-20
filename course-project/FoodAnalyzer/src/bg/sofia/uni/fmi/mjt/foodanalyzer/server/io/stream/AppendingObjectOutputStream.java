package bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.stream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public final class AppendingObjectOutputStream extends ObjectOutputStream {

    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // reset header because we want to append new object
        // showed a problem with the original
        reset();
    }
}
