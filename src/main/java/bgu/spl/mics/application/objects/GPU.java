package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.services.GPUService;

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

    public synchronized void trainData(){
        while (!terminate) {
            time = time + 1;
            if (speed <= time) {
                db = cluster.getNextProcessedData(this);
                if (db == null)
                    break;
                if (db.getData().processData())
                    gpuService.doneTraining(db);
                time = time - speed;
            }
            try {
                this.wait();
            } catch (InterruptedException e) {
                //do nothing;
            }
        }
    }

    public void addTime(){ time++; }

    public boolean isAvailable(){ return available; }

    public void trainModelEvent (Model model){
        available = false;
        this.model = model;
    }
    
    public void startModelTraining(Model model){}

    public Type getType() {return type;}

    public Future testModel(Model model){
        return null;
    }

}
