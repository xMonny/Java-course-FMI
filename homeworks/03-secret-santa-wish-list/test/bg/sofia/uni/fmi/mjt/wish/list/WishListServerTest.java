package bg.sofia.uni.fmi.mjt.wish.list;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WishListServerTest {

    @Test
    public void testStopWhenNotStarted() {
        WishListServer server = new WishListServer(3333);
        boolean hasStarted = server.isOpened();
        server.stop();

        assertFalse("You cannot close server when it wasn't opened", hasStarted);
    }

    @Test
    public void testStart() {
        WishListServer server = new WishListServer(3333);
        server.start();
        boolean hasStarted = server.isOpened();
        server.stop();

        assertTrue(hasStarted);
    }

    @Test
    public void testStop() throws InterruptedException {
        WishListServer server = new WishListServer(3333);
        server.start();
        Thread.sleep(4);
        server.stop();
        assertFalse(server.isOpened());
    }
}
