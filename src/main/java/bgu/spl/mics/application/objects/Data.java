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
    private int size;
    private final GPU gpu;

    //-----------------Constructor-------------------
    public Data(Type _type, int _size){
        this.type = _type;
        this.size = _size;
        this.gpu = null;
    }

    public Data(Type _type, int _size,GPU gpu){
        this.type = _type;
        this.size = _size;
        this.gpu = gpu;
    }
    //-------------------Methods---------------------
    public Type getType(){
        return this.type;
    }

    public int getSize(){return size;}

    public Boolean processData(){
        return processed.incrementAndGet() * 1000 >= size;
    }

//    public void processData(int numOfProcesses){
//        if ( processed == size )
//            throw new StackOverflowError("All data has been processed"); //TODO: which exception should it throw
//        else if ( processed + numOfProcesses >= size )
//            processed = size;
//        else
//            processed += numOfProcesses;
//    }

    public GPU getGpu(){return gpu;}
}
