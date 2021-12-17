package bgu.spl.mics.application.objects;

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

    public void startUp(DataBatch db){this.db = db; calculationTime = (db.getData().getSpeed()) / cores;}



    public  void processData() {
        while (!terminate) {
            time = time + 1;
            if (calculationTime <= time) {
                cluster.addProcessedData(db);
                db = cluster.getNextDataToBePreprocessed();
                calculationTime = (db.getData().getSpeed()) / cores;
                time = time - calculationTime;
            }
            try {
                this.wait();
            } catch (InterruptedException e) {
                //do nothing;
            }
        }
    }

    public void updateTime(){time++;}

}
