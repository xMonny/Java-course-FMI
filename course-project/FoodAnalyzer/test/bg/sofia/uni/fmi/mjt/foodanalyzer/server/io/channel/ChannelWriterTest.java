package bg.sofia.uni.fmi.mjt.foodanalyzer.server.io.channel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@RunWith(MockitoJUnitRunner.class)
public class ChannelWriterTest {
    @Mock
    private final SocketChannel socketChannelMock = Mockito.mock(SocketChannel.class);

    @Mock
    private final ByteBuffer bufferMock = Mockito.mock(ByteBuffer.class);

    @Test(expected = IllegalArgumentException.class)
    public void testChannelWriterWithNullSocketChannel() {
        new ChannelWriter(null, bufferMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChannelWriterWithNullBuffer() {
        new ChannelWriter(socketChannelMock, null);
    }
}
