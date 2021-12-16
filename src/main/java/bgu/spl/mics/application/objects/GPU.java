package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080, TEST}

    private Type type;
    private Model model = null;
    private Cluster cluster;
    private boolean available = true;

    public GPU(Type type){
        this.type = type;
    }

    public GPU (String type){
        if (type.equals("RTX3090"))
            this.type = Type.RTX3090;
        else if (type.equals("RTX2080"))
            this.type = Type.RTX2080;
        else
            this.type = Type.GTX1080;
    }

    public boolean isAvailable(){return available;}

    public void trainModelEvent (Model model){
        available = false;
        this.model = model;
    }
    
    public void startModelTraining(Model model){}

    public Future testModel(Model model){
        return null;
    }

}
