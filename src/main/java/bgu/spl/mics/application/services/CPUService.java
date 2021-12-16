package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.broadcast.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.DataBatch;

/**
 * CPU service is responsible for handling the {@link  //DataPreProcessEvent}.
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
    private int time=0;
    private int speed;
    private Cluster cluster;
    DataBatch db;
    int calculationTime;
    int coreNum;


    //-----------------Constructor---------------------
    public CPUService(String name, CPU _cpu) {
        super(name);
        cpu = _cpu;
        cluster = Cluster.getInstance();
        coreNum = cpu.getNumOfCores();
    }

    //-------------------Methods----------------------

    private void processData(){
        time = time + 1;
        if (calculationTime <= time) {
            cluster.addProcessedData(db);
            db = cluster.getNextDataToBePreprocessed();
            calculationTime = (db.getData().getSpeed())/coreNum;
        }
    }

    @Override
    protected void initialize() {
        Thread processDataThread = new Thread(cpu::processData);
        db = cluster.getNextDataToBePreprocessed();
        calculationTime = (db.getData().getSpeed())/coreNum;

        processDataThread.start();

        subscribeBroadcast(TickBroadcast.class, c -> {
            if (processDataThread.getState() == Thread.State.WAITING) {
                processDataThread.notify();
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
            cpu.terminate();
            processDataThread.notify();
            terminate();
        });
    }
}
