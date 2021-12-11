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

    //-----------------Constructor-------------------
    public DataBatch(Data _data, int _index){
        this.data = _data;
        this.startIndex = _index;
    }

    //-------------------Methods---------------------

    public void setStartIndex(){
        this.startIndex ++;
        data.processData();
    }

    public void setStartIndex(int nextIndex){
        data.processData(nextIndex);
        this.startIndex += nextIndex;
    }

    
}
