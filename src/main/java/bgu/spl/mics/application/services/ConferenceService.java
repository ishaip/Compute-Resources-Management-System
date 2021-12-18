package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.PublishConferenceBroadcast;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.events.PublishResultEvent;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.ArrayList;
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
    private ArrayList<Model> models = new ArrayList<>();
    private int time=0;
    private int date;
    private ConfrenceInformation confrenceInformation;


    public ConferenceService(String name, ConfrenceInformation confrenceInformation) {
        super(name);
        this.confrenceInformation = confrenceInformation;
        this.date = confrenceInformation.getDate();
    }

    @Override
    protected void initialize() {
        System.out.println("Conference is now online  " + getName());
        subscribeBroadcast(TickBroadcast.class,c -> {
            time ++;
            if (time > date){
                sendBroadcast(new PublishConferenceBroadcast(models));
                System.out.println("out of time" + getName());
                terminate();
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
            System.out.println("Conference is being terminated" +getName());
            terminate();
        });

        subscribeEvent(PublishResultEvent.class , c -> {
            System.out.println("gotten a model!!!!!!!!");
            models.add(c.getModel());
            confrenceInformation.addPublication(c.getModel());
        });
    }
}
