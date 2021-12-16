package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.PublishConferenceBroadcast;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.events.TestModelEvent;
import bgu.spl.mics.application.events.TrainModelEvent;
import bgu.spl.mics.application.objects.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static bgu.spl.mics.application.objects.Model.Status.Trained;

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
    private Cluster cluster;
    private int speed;
    private ConcurrentHashMap<Data, Future<Model.Status>> modelFutures = new ConcurrentHashMap<>();


    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
        cluster = Cluster.getInstance();
        if (gpu.getType() == GPU.Type.GTX1080)
            speed = 4;
        if (gpu.getType() == GPU.Type.RTX2080)
            speed = 2;
        if (gpu.getType() == GPU.Type.RTX3090)
            speed = 1;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, c -> {
            DataBatch db;
            time = time + 1;
            if (speed <= time ){
                db = cluster.getNextProcessedData();
                if (db.getData().processData())
                    modelFutures.get(db.getData()).resolve(Trained);
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
             terminate();
         });

        subscribeEvent(TrainModelEvent.class, c-> {
            modelFutures.putIfAbsent(c.getData(),c.getFuture());
            for(int i =0; i < c.getData().getSize(); i +=1000){
                DataBatch db = new DataBatch(c.getData(),0,gpu);
                cluster.addDataToBePreprocessed(db);
            }
        });
    }
}
