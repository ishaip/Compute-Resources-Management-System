package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.services.GPUService;

import java.awt.color.CMMException;
import java.util.LinkedList;
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
    private Data data = null;
    private LinkedList<Data> dataList = new LinkedList<>();
    private int dataInProsse=0;

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
            if (data != null && data.isDone())
                data = dataList.pollFirst();
            while(data != null ) {
                while (dataInProsse < 100 && !data.isDone(dataInProsse)) {
                    DataBatch db = new DataBatch(data, this);
                    try {
                        cluster.addDataToBePreprocessed(db);
                        dataInProsse++;
                    } catch (InterruptedException e) {
                        terminate = true;
                        break;
                    }
                }
            }
            if (terminate)
                break;
            time++;
            CRMSRunner.cpuTimeUsed.incrementAndGet();
            if (time >= speed) {
                if (cluster.getNextProcessedData(this) == null) {
                    terminate = true;
                    break;
                }
                dataInProsse--;
                if (data.isDone()){
                    gpuService.doneTraining(data);
                    data =null;
                }
            }
            try {
                this.wait();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public synchronized void getMoreTime(){notify();}

    public void addTime(){ time++; }

    public boolean isAvailable(){ return available; }

    public void setData(Data data) {
        if (this.data == null)
            this.data = data;
        else dataList.add(data);
    }

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
