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
	private int speed;
	private int duration;


	public TimeService(String name, int speed, int duration) {
		super(name);
		time = 0;
		this.speed=speed;
		this.duration=duration;
	}

	@Override
	protected void initialize() {
		while(time < duration) {
			time = time + 1;
			MessageBusImpl.getInstance().sendBroadcast(new TickBroadcast(time));
			try {
				wait(speed);
			} catch (InterruptedException e) {
				//do nothing
			}
		}
		MessageBusImpl.getInstance().sendBroadcast(new TerminateBroadcast());
	}

}
