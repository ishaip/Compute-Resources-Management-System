package bgu.spl.mics;

import bgu.spl.mics.application.services.TBroadcast;
import bgu.spl.mics.application.services.TEvent;
import bgu.spl.mics.application.services.TMicroService;
import junit.framework.TestCase;
import org.junit.Test;import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class MessageBusImplTest extends TestCase {

    private MessageBusImpl tBus;

    @BeforeEach
    void setup(){
        MessageBusImpl tBus = new MessageBusImpl();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testSubscribeEvent() {
        //tested in testSendEvent
    }

    @Test
    public void testSubscribeBroadcast() {
        //tested in testSendBroadcast
    }

    @Test
    public void testComplete() {
        /*
        create an event with result 0
        complete with result 1
        assert that event result is 1
         */
        MicroService m1 = new TMicroService();
        tBus.register(m1);
        tBus.subscribeEvent(TEvent.class,m1);
        TEvent e = new TEvent(0);
        Future<Integer> future = tBus.sendEvent(e);
        assertNotNull(future);
        //testing the method
        tBus.complete(e,1);
        //asserting that the future have the correct value
        assertTrue(future.isDone());
        assertEquals(1,(int)future.get());
    }

    @Test
    public void testSendBroadcast() {
        //making 2 microServices and seeing if the can subscribe and if the queue contains them
        //init
        MicroService m1 = new TMicroService();
        MicroService m2 = new TMicroService();
        tBus.register(m1);
        tBus.register(m2);
        TBroadcast b = new TBroadcast();
        tBus.subscribeBroadcast(TBroadcast.class, m2);
        tBus.subscribeBroadcast(TBroadcast.class, m1);
        tBus.sendBroadcast(b);
        try {
            assertEquals(b, tBus.awaitMessage(m1));
            assertEquals(b, tBus.awaitMessage(m2));
        } catch (Exception ignored) {
            fail("broadcast hasn't arrived");
        }
    }

    @Test
    public void testSendEvent() {
        //making 2 microServices
        //making sure the events were delivered
        //sending 3 events and making sure they are being delivered in a round-robin manner

        //init
        TMicroService m1 = new TMicroService();
        TMicroService m2 = new TMicroService();
        tBus.register(m1);
        tBus.register(m2);
        assertNull(tBus.sendEvent(new TEvent(10)));
        //making sure that no future is sent if there is no subs
        tBus.subscribeEvent(TEvent.class,m1);
        tBus.subscribeEvent(TEvent.class,m2);
        TEvent[] events = new TEvent[3];
        for (int i = 0; i < 3;i++){
            events[i] = new TEvent(i);
            assertNotNull(tBus.sendEvent(events[i])); //we now have m1,m2 subscribed
        }
        try{
            Message message;
            message = tBus.awaitMessage(m1);
            assertEquals(events[0],message);
            message = tBus.awaitMessage(m2);
            assertEquals(events[1],message);
            message = tBus.awaitMessage(m1);
            assertEquals(events[2],message);
        }catch (InterruptedException exp){
            fail("event hasent been deliverd");
        }
    }

    @Test
    public void testRegister() {
        //was tested in other methods such as testSendBroadcast
    }

    public void testUnregister() {
        //we will register 1 microservices
        // and will subscribe to an event
        // and see if a future is called back using send event
        TMicroService m1 = new TMicroService();
        tBus.register(m1);
        tBus.subscribeEvent(TEvent.class,m1);
        tBus.unregister(m1);
        assertNull(tBus.sendEvent(new TEvent(1)));
    }

    @Test
    public void testAwaitMessage() {
        //is tested in testSendEvent

    }
}