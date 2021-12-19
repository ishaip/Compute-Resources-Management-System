package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;

import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class TimeService extends MicroService{
	private int time;
	private final int speed;
	private final int duration;
	private MessageBusImpl mb;


	public TimeService(String name, int speed, int duration) {
		super(name);
		time = 0;
		this.speed=speed;
		this.duration=duration;
	}

	@Override
	protected synchronized void initialize() {
		while(time < duration) {
			time = time + 1;
			sendBroadcast(new TickBroadcast());
			try {
				wait(speed);
			} catch (InterruptedException e) {
				break;
			}
		}
		sendBroadcast(new TerminateBroadcast());
		terminate();
	}
}
