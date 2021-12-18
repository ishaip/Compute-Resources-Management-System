package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.CRMSRunner;

import java.util.ArrayList;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    //---------------------Fields----------------------
    /**
     * @INV: data.size() <= 1000
     *       cores > 0
     */
    private int cores;
    private Cluster cluster = Cluster.getInstance();
    private int calculationTime;
    private int time = 0;
    private DataBatch db;
    private boolean terminate = false;
    private boolean isDone = true;

    //-----------------Constructor---------------------
    public CPU(int numOfCores){
        this.cores = numOfCores;

        /* Cluster */
    }

    //-------------------Methods-----------------------
    public int getNumOfCores(){
        return this.cores;
    }

    public void terminate(){terminate = true;}

    public void startUp(DataBatch db){
        this.db = db;
        calculationTime = (db.getData().getSpeed()) / cores;
    }



    public synchronized void processData() {
        while (!terminate) {
            time ++;
            CRMSRunner.cpuTimeUsed.incrementAndGet();

            try {
                this.wait();
            } catch (InterruptedException e) {
                //do nothing;
            }
        }
    }

    public synchronized void getMoreTime(){notify();}

    public void updateTime(){time++;}

    public boolean isDone() { return isDone;}

    public void testprocess(ArrayList<DataBatch> s) { isDone = false;
    }

    public void testaddBatchOfData(DataBatch db) {
    }
}
