package bg.sofia.uni.fmi.mjt.foodanalyzer.client.io;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.channel.ChannelReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@RunWith(MockitoJUnitRunner.class)
public class ServerWriterTest {
    @Mock
    private final SocketChannel socketChannelMock = Mockito.mock(SocketChannel.class);

    @Mock
    private final ByteBuffer bufferMock = Mockito.mock(ByteBuffer.class);

    @Test(expected = IllegalArgumentException.class)
    public void testServerWriterWithNullSocketChannel() {
        new ServerWriter(null, bufferMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testServerWriterWithNullBuffer() {
        new ServerWriter(socketChannelMock, null);
    }
}
