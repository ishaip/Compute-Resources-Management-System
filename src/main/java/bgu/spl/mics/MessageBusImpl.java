package bgu.spl.mics;

import bgu.spl.mics.application.broadcast.TerminateBroadcast;

import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {
	private static MessageBusImpl instance = null;
	private static volatile boolean  isDone = false;
	//using ConcurrentHashMap and Blocking queue because it will be better for threading
	//a queue of messages for every microService
	private ConcurrentHashMap<MicroService,BlockingQueue<Message>> microServiceMessageQueues = new ConcurrentHashMap<>();
	//a queue of MicroServices subscribed to a message type for every message type
	private ConcurrentHashMap<Class<? extends Message>,BlockingQueue<MicroService>> messageRegisteredQueues = new ConcurrentHashMap<>();
	//a queue of futures for every event
	private ConcurrentHashMap<Event<?>,Future> eventFutureQueues = new ConcurrentHashMap<>();

	public static MessageBusImpl getInstance(){
		if (!isDone){
			synchronized (MessageBusImpl.class){
				if (!isDone){
					instance = new MessageBusImpl();
					isDone = true;
				}
			}
		}
		return instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) { subscribeMessage(type,m);}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) { subscribeMessage(type,m);}

	//new function needed the signature of the functions given aren't generic Message
	private void subscribeMessage(Class<? extends Message> type, MicroService m) {
		//making sure we have microservice registered in the message bus
		if (!microServiceMessageQueues.containsKey(m))
			throw new NullPointerException("the key isn't registered,probably threading mistake");
		messageRegisteredQueues.putIfAbsent(type,new LinkedBlockingQueue<>());
		//we want to add the value to the proper queue and if it is all ready there do nothing
		BlockingQueue<MicroService> messageRegisteredQueue = messageRegisteredQueues.get(type);
		if (messageRegisteredQueue != null && !messageRegisteredQueue.contains(m)){
			//may change threading
			messageRegisteredQueue.add(m);
		}
	}


	@Override
	public <T> void complete(Event<T> e, T result) {
		if (!eventFutureQueues.containsKey(e))
			throw new IllegalArgumentException("event doesn't exsit any more");
		else
			eventFutureQueues.get(e).resolve(result);
		//unchecked
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		/*
		if (b.getClass() == TerminateBroadcast.class)
			if (messageRegisteredQueues.containsKey(b.getClass()))
				for (MicroService ms : messageRegisteredQueues.get(b.getClass()))
					ms.terminate();
		else
			*/
			if (messageRegisteredQueues.containsKey(b.getClass()))
				for (MicroService ms : messageRegisteredQueues.get(b.getClass()))
					microServiceMessageQueues.get(ms).add(b);
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		//checking that there exists threads that are registered to deal with the event
		if (!messageRegisteredQueues.containsKey(e.getClass()))
			return null;
		//the round robbing manor will be used here by shifting around the messageRegisteredQueues
		//need to make sure the order is maintained, and since we are removing temporally the thread we will make sure there always exists one using synchronization
		synchronized (messageRegisteredQueues.get(e.getClass())){
			if (messageRegisteredQueues.containsKey(e.getClass()) || messageRegisteredQueues.get(e.getClass()).isEmpty())
				return null;
			//the handler was snatched or there are no handlers
			MicroService handler = messageRegisteredQueues.get(e.getClass()).poll();
			Future<T> future = new Future<>();
			microServiceMessageQueues.get(handler).add(e);
			messageRegisteredQueues.get(e.getClass()).add(handler);
			return future;
		}
	}

	@Override
	public void register(MicroService m) {
		microServiceMessageQueues.putIfAbsent(m, new LinkedBlockingQueue<Message>());}

	@Override
	public void unregister(MicroService m) {
		for(BlockingQueue<MicroService>  bq : messageRegisteredQueues.values()){
				bq.remove(m);
		}
		if (!microServiceMessageQueues.containsKey(m))
			throw new IllegalArgumentException("microService was allready unregistered");
		microServiceMessageQueues.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(microServiceMessageQueues.containsKey(m))
			return microServiceMessageQueues.get(m).take();
		else
			throw new NullPointerException("no such microService is registered");
	}
}
