package bgu.spl.mics;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.TimeUnit;

public class FutureTest extends TestCase {

    private Future<String> future;

    @BeforeEach
    public void setUp() throws Exception {
        future = new Future<>();
    }

    public void tearDown() throws Exception {
    }

    @Test
    public void testGet() {
        assertFalse(future.isDone());
        future.resolve("");
        future.get();
        assertTrue(future.isDone());
    }

    @Test
    public void testResolve() {
        String str = "result";
        future.resolve(str);
        assertTrue(future.isDone());
        assertEquals(str, future.get());
    }

    @Test
    public void testIsDone() {
        String str = "result";
        assertFalse(future.isDone());
        future.resolve(str);
        assertTrue(future.isDone());
    }

    @Test
    public void testTestGet() throws InterruptedException{
        String str = "result";
        assertFalse(future.isDone());
        future.get(100, TimeUnit.MILLISECONDS);
        assertFalse(future.isDone());
        future.resolve(str);
        assertEquals(future.get(100, TimeUnit.MILLISECONDS), str);
    }
}