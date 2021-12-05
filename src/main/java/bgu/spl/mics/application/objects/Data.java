package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    enum Type {
        Images, Text, Tabular
    }

    /**
     * @INV: processed >=0
     *       processed <= size
     */
    //--------------------Fields---------------------
    private Type type;
    private int processed;
    private int size;

    //-----------------Constructor-------------------
    public Data(Type _type, int _size){
        this.type = _type;
        this.processed = 0;
        this.size = _size;
    }

    //-------------------Methods---------------------
    public Type getType(){
        return this.type;
    }

    public void processData(){
        processed ++;
    }

}
