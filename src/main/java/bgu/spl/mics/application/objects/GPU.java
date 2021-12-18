package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.services.GPUService;
import sun.misc.Queue;

import java.awt.color.CMMException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static bgu.spl.mics.application.objects.Model.Status.Trained;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {


    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080, TEST}

    private final Type type;
    private Model model = null;
    private final Cluster cluster = Cluster.getInstance();
    private boolean available;
    private int speed;
    private int time = 0;
    private DataBatch db;
    private boolean terminate = false;
    private GPUService gpuService;
    private Data data;
    private int dataInProsse=0;
    private ArrayDeque<Model> models = new ArrayDeque<>();
    public GPU(Type type){
        this.type = type;
        available = true;
        if (this.type == GPU.Type.GTX1080)
            speed = 4;
        if (this.type == GPU.Type.RTX2080)
            speed = 2;
        if (this.type == GPU.Type.RTX3090)
            speed = 1;
        cluster.startNewGpuConnection(this);
    }

    public GPU (String type){
        available = true;
        if (type.equals("GTX1080")){
            this.type = Type.GTX1080;
            this.speed = 4;
        }
        else if (type.equals("RTX2080")){
            this.type = Type.RTX2080;
            this.speed = 2;
        }
        else{
            this.type = Type.RTX3090;
            this.speed = 1;
        }
        cluster.startNewGpuConnection(this);
    }

    public void terminate() { terminate=true; }

    public void setGpuService(GPUService gpuService){ this.gpuService = gpuService; }

    public void processGPUData() {
        if (speed <= time) {
            db.Processe();
            if (db.getData().isDone()) {
                gpuService.doneTraining(db);
                model = models.poll();
                if (model != null) {
                    data = model.getData();
                    for (int i = 0; i < 8; i++) {
                        DataBatch nextData = data.getNextDataBatch(this);
                        if (nextData != null)
                            cluster.addDataToBePreprocessed(nextData);
                    }
                }
            } else {
                DataBatch nextData = data.getNextDataBatch(this);
                if (nextData != null)
                    cluster.addDataToBePreprocessed(nextData);
            }

            time = time - speed;
        }
    }

    private void initialize(Model model){
        this.model = model;
        this.data =  model.getData();
        this.db = data.getNextDataBatch(this);

    }


    public boolean hasDataToBeTrained(){return db != null;}

    public void pullNewData(){
        db = cluster.getNextProcessedData(this);
    }

    public synchronized void getMoreTime(){notify();}

    public void addTime(){
        if (db != null) {
            time++;
            CRMSRunner.gpuTimeUsed.incrementAndGet();
        }
    }

    public boolean isAvailable(){ return available; }

    public void setData(Data data) {this.data = data;}

    public void addModel(Model model){
        if (models.isEmpty())
            initialize(model);
        else
            models.addLast(model);
    }

    public boolean hasModelToBeProcessed(){return !models.isEmpty();}

    public void trainModelEvent (Model model){
        available = false;
        this.model = model;
    }
    
    public void startModelTraining(Model model){available = false;}

    public Type getType() {return type;}

    public Future testModel(Model model){
        return null;
    }

}
