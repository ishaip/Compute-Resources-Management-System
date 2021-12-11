package bgu.spl.mics.application.objects;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Statistics {

    //---------------------Fields----------------------
    private ArrayList<String> trainedModels = new ArrayList<>();
    private int DataBatchProcessed = 0; // total number of DataBatches processed by the CPU
    private int CPUTimeUnitUsed = 0; // represent the number of CPU time unit used
    private int GPUTimeUnitUsed = 0; // represent the number of GPU time unit used

    //------------------Constructor---------------------
    public Statistics(){}

    //-------------------Methods-----------------------
    public void addTrainedModel (String model){
        trainedModels.add(model);
    }

    public void addTrainedModel (ArrayList<String> models){
        trainedModels.addAll(models);
    }

    public ArrayList<String> getTrainedModels (){
        return trainedModels;
    }

    public void setDataBatchProcessed (int processed){
        DataBatchProcessed += processed;
    }

    public int getDataBatchProcessed (){
        return DataBatchProcessed;
    }

    public void setCPUTimeUnitUsed (int units){
        CPUTimeUnitUsed += units;
    }

    public int getCPUTimeUnitUsed() {
        return CPUTimeUnitUsed;
    }

    public void setGPUTimeUnitUsed (int units){
        GPUTimeUnitUsed += units;
    }

    public int getGPUTimeUnitUsed() {
        return GPUTimeUnitUsed;
    }
}
