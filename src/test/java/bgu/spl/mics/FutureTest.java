package bgu.spl.mics;

import junit.framework.TestCase;

public class FutureTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public FutureTest testGet() {
        return this;
    }

    public void testResolve() {
        Object o = new Future();
        try {

        } catch (Exception e) {
            System.out.println("Object has wrong value");
        }
    }

    public void testIsDone() {
    }

    public void testTestGet() {
    }
}