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

//    @Test
//    public void testGet() {
//        assertFalse(future.isDone());
//        future.resolve("");
//        // the rest of the code was without 'try' and 'catch'
//        try {
//            future.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        assertTrue(future.isDone());
//    }
//
//    @Test
//    public void testResolve() {
//        String str = "result";
//        future.resolve(str);
//        assertTrue(future.isDone());
//        // the rest of the code was without 'try' and 'catch'
//        try {
//            assertEquals(str, future.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testIsDone() {
//        String str = "result";
//        assertFalse(future.isDone());
//        future.resolve(str);
//        assertTrue(future.isDone());
//    }
//
//    @Test
//    public void testTestGet() throws InterruptedException{
//        String str = "result";
//        assertFalse(future.isDone());
//        future.get(100, TimeUnit.MILLISECONDS);
//        assertFalse(future.isDone());
//        future.resolve(str);
//        assertEquals(future.get(100, TimeUnit.MILLISECONDS), str);
//    }
}