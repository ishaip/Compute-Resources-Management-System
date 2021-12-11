package bgu.spl.mics.application.objects;

import java.util.Collection;
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
    private boolean done;
    private ArrayList<DataBatch> data;
    private Cluster cluster;

    //----------------Constructor-----------------------
    public CPU(int numOfCores){
        this.cores = numOfCores;
        this.done = false;
        this.data = new ArrayList<DataBatch>();
        /* Cluster */
    }

    //-------------------Methods-----------------------
    public int getNumOfCores(){
        return this.cores;
    }

    /**
     * @Post: done == true
     * @param dataToProcess
     */
    public void process(ArrayList<DataBatch> dataToProcess){
        //some processing
        this.done = true;
    }

    /**
     * @Post: done == true
     */
    public void process(){
        //some processing
        this.done = true;
    }

    public boolean isDone(){
        return done;
    }

    /**
     * @Post: data.size() == @Pre(data.size()) + 1
     * @param db
     */
    public void addBatchOfData(DataBatch db){
        this.data.add(db);
    }
}
