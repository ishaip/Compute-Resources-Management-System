package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.PublishConferenceBroadcast;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.events.TestModelEvent;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Student;

/**
 * GPU service is responsible for handling the
 * {@link //TrainModelEvent} and {@link //TestModelEvent},
 * in addition to sending the {@link //DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
    private GPU gpu;
    private int time = 0;


    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, c -> {
            time = c.getTick();
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
             terminate();
         });

        subscribeEvent(TestModelEvent.class, c-> {

        });
    }
}
