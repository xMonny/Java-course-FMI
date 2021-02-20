package bg.sofia.uni.fmi.mjt.foodanalyzer.client.io;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerReaderTest {
    @Mock
    private final SocketChannel socketChannelMock = Mockito.mock(SocketChannel.class);

    @Mock
    private final ByteBuffer bufferMock = Mockito.mock(ByteBuffer.class);

    @Test(expected = IllegalArgumentException.class)
    public void testServerReaderWithNullSocketChannel() {
        new ServerReader(null, bufferMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testServerReaderWithNullBuffer() {
        new ServerReader(socketChannelMock, null);
    }
}
