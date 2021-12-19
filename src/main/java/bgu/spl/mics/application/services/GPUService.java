package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.events.TestModelEvent;
import bgu.spl.mics.application.events.TrainModelEvent;
import bgu.spl.mics.application.objects.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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
    private final GPU gpu;
    private int counter = 0;
    private final Cluster cluster = Cluster.getInstance();
    private final ConcurrentHashMap<Data, Future<Model.Status>> modelFutures = new ConcurrentHashMap<>();
    private TrainModelEvent c;


    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
    }

    public void sendDataBatches() {

    }

    public void doneTraining(DataBatch db){
        modelFutures.get(db.getData()).resolve(Trained);
        //System.out.println(db.getData().getGpu()); //TODO were printing bull
    }

    @Override
    protected void initialize() {
        cluster.startNewGpuConnection(gpu);
        gpu.setGpuService(this);

        subscribeBroadcast(TickBroadcast.class, c -> {
            gpu.addTime();
            if(gpu.hasDataToBeTrained()) {
                gpu.processGPUData();
            }
            else
                gpu.pullNewData();
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
             terminate();
         });

        subscribeEvent(TrainModelEvent.class, c-> {
            modelFutures.put(c.getData(),c.getFuture());
            gpu.addModel(c.getModel());
        });

        subscribeEvent(TestModelEvent.class, c-> {
            c.setStatus(Model.Status.Tested);
            if (c.getStudent().getDegree() == Student.Degree.MSc) {
                if (Math.random() >= 0.4)
                    c.setResult(Model.Result.Good);
                else
                    c.setResult(Model.Result.Bad);
            }
            if (c.getStudent().getDegree() == Student.Degree.PhD) {
                if (Math.random() >= 0.2)
                    c.setResult(Model.Result.Good);
                else
                    c.setResult(Model.Result.Bad);
            }
            c.getFuture().resolve(c.getResult());
        });
        CRMSRunner.threadInitCounter.countDown();
    }


}
