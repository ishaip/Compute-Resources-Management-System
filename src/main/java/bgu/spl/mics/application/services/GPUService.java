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


    public GPUService(String name, GPU gpu) {
        super(name);
        this.gpu = gpu;
    }

    public void doneTraining(DataBatch db){
        modelFutures.get(db.getData()).resolve(Trained);
    }

    @Override
    protected void initialize() {
        cluster.startNewGpuConnection(gpu);
        gpu.setGpuService(this);
        Thread trainDataThread = new Thread(gpu::trainData) ;
        trainDataThread.start();

        subscribeBroadcast(TickBroadcast.class, c -> {
            counter++;
            if ((counter -1 )% 1000  == 1)
                System.out.println(counter);
            gpu.getMoreTime();
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
            gpu.terminate();
            trainDataThread.interrupt();
             terminate();
         });

        subscribeEvent(TrainModelEvent.class, c-> {
            System.out.println("gfgf");
            modelFutures.putIfAbsent(c.getData(),c.getFuture());
            for(int i =0; i < c.getData().getSize(); i +=1000){
                DataBatch db = new DataBatch(c.getData(),0,gpu);
                cluster.addDataToBePreprocessed(db);
            }
        });

        subscribeEvent(TestModelEvent.class, c-> {
            if (c.getStudent().getDegree() == Student.Degree.MSc) {
                if (Math.random() >= 0.4)
                    c.getFuture().resolve(Model.Result.Good);
                else
                    c.getFuture().resolve(Model.Result.Bad);
            }
            if (c.getStudent().getDegree() == Student.Degree.PhD) {
                if (Math.random() >= 0.6)
                    c.getFuture().resolve(Model.Result.Good);
                else
                    c.getFuture().resolve(Model.Result.Bad);
            }
        });
        CRMSRunner.threadInitCounter.countDown();
    }
}
