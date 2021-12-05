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
    private int core;
    private boolean done;
    private ArrayList<DataBatch> data;
    private Cluster cluster;

    //----------------Constructor-----------------------
    public CPU(){
        this.done = false;
        this.data = new ArrayList<DataBatch>();

    }

    //-------------------Methods-----------------------
    public int getNumOfCPUs(){
        return this.core;
    }

    public void process(){
        /*

         */
        this.done = true;
    }

    public boolean isDone(){
        return done;
    }

}
