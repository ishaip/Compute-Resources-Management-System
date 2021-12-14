package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;

/**
 * CPU service is responsible for handling the {@link  DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    //---------------------Fields----------------------
    private boolean terminated;
    private String name;
    private CPU cpu; //

    //-----------------Constructor---------------------
    public CPUService(String name) {
        super(name);
        // TODO Implement this
    }

    //-------------------Methods----------------------
    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeBroadcast(TickBroadcast.class, callback -> {
           int time = callback.getTick();
        });
        terminated = true;
        while (terminated){
            cpu.process();
        }
    }
}
