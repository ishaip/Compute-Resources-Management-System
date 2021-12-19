package bgu.spl.mics.application.objects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    public enum Type {
        Images, Text, Tabular
    }

    /**
     * @INV: processed >=0
     *       processed <= size
     */
    //--------------------Fields---------------------
    private Type type;
    private AtomicInteger processed = new AtomicInteger();
    private AtomicInteger dataBatchesInProcessing = new AtomicInteger(0);
    private final int size;
    private final GPU gpu;
    private int speed;

    //-----------------Constructor-------------------
    public Data(Type _type, int _size){
        this.type = _type;
        this.size = _size;
        this.gpu = null;
        if (type == Type.Images)
            speed = 128;
        if (type == Type.Text)
            speed = 64;
        if (type == Type.Tabular)
            speed = 32;
    }

    public Data(Type _type, int _size,GPU gpu){
        this.type = _type;
        this.size = _size;
        this.gpu = gpu;
        if (type == Type.Images)
            speed = 128;
        if (type == Type.Text)
            speed = 64;
        if (type == Type.Tabular)
            speed = 32;
    }
    //-------------------Methods---------------------
    public Type getType(){
        return this.type;
    }

    public DataBatch getNextDataBatch(GPU gpu){
        dataBatchesInProcessing.incrementAndGet();
        return new DataBatch(this, 0, gpu);
    }

    public int getSize(){ return size; }

    public void processData(){processed.incrementAndGet();}

    public boolean allDataReleased(){ return (dataBatchesInProcessing.get() + processed.get())*1000 > size ;}

    public Boolean isDone(){
        if((processed.get() ) * 1000 >= size)
            System.out.println("here");
        return ((processed.get() ) * 1000 >= size);
    }

    public int dataToPross(){return size - processed.get();}

    public int getSpeed(){ return speed; }

//    public void processData(int numOfProcesses){
//        if ( processed == size )
//            throw new StackOverflowError("All data has been processed");
//        else if ( processed + numOfProcesses >= size )
//            processed = size;
//        else
//            processed += numOfProcesses;
//    }

    public GPU getGpu(){ return gpu; }

    public String toString(){
        String str = "";

        str += "\t\t\t\t\t\t\"type\": \"" + type + "\",\n";
        str += "\t\t\t\t\t\t\"size\": " + Integer.toString(size) + "\n";

        return str;
    }
}
