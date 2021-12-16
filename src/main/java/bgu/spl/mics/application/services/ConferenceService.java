package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.PublishConferenceBroadcast;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.events.PublishResultEvent;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.LinkedList;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link //PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */


public class ConferenceService extends MicroService {
    private LinkedList<Model> models = new LinkedList<Model>();
    private int time=0;
    private int date;


    public ConferenceService(String name, int date) {
        super(name);
        this.date = date;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class,c -> {
            time = c.getTick();
            if (time > date){
                sendBroadcast(new PublishConferenceBroadcast(models));
                terminate();
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
            terminate();
        });

        subscribeEvent(PublishResultEvent.class , c -> {
            models.add(c.getModel());
        });
    }
}
