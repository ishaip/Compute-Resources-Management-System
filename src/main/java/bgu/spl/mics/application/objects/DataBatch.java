package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch { //

    /** @INV: startIndex >= 0
     *        startIndex < data.size()
     */
    //--------------------Fields---------------------
    private Data data;
    private int startIndex;
    private GPU gpu;

    //-----------------Constructor-------------------
    public DataBatch(Data _data, int _index){
        this.data = _data;
        this.startIndex = _index;
    }

    public DataBatch(Data _data, int _index,GPU gpu){
        this.data = _data;
        this.startIndex = _index;
        this.gpu = gpu;
    }

    //-------------------Methods---------------------
    public void setStartIndex(){
        this.startIndex ++;
        data.processData();
    }

    public void Processe(){data.processData();}

    public void setStartIndex(int nextIndex){
        this.startIndex += nextIndex;
    }

    public Data getDataType() { return data; }

    public GPU getGpu() {return gpu;}

    public Data getData(){return  data;}
}
