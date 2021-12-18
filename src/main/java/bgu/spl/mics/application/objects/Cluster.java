package bgu.spl.mics.application.objects;
import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {
		if (!isDone){
			synchronized (MessageBusImpl.class){
				if (!isDone){
					instance = new Cluster();
					isDone = true;
				}
			}
		}
		return instance;
	}

	//---------------------Fields----------------------
	private static volatile boolean isDone = false;
	private ArrayList<GPU> gpus;
	private ArrayList<CPU> cpus;
	private Statistics stats;
	private static Cluster instance = null;
	private LinkedBlockingQueue<DataBatch> dataToProcessed = new LinkedBlockingQueue<DataBatch>();
	private ConcurrentHashMap<GPU,LinkedBlockingQueue<DataBatch>> processedData = new ConcurrentHashMap<GPU,LinkedBlockingQueue<DataBatch>>();

	//------------------Constructor---------------------
	public Cluster(){
		this.gpus = new ArrayList<>();
		this.cpus = new ArrayList<>();
		this.stats = new Statistics();
	}

	public Cluster(ArrayList<GPU> g, ArrayList<CPU> c, Statistics s){
		this.gpus = new ArrayList<>(g);
		this.cpus = new ArrayList<>(c);
		this.stats = s;
	}

	//-------------------Methods-----------------------
	public void startNewGpuConnection(GPU gpu){processedData.putIfAbsent(gpu,new LinkedBlockingQueue<DataBatch>());}

	public ArrayList<CPU> getCpus() {
		return cpus;
	}

	public ArrayList<GPU> getGpus() {
		return gpus;
	}

	public void addDataToBePreprocessed(DataBatch db) {
		try {
			dataToProcessed.put(db);
		} catch (InterruptedException e) {
			//do nothing
		}
	}

	public DataBatch getNextProcessedData(GPU gpu){
		try {
			DataBatch bd = processedData.get(gpu).take();
			bd.getData().processData();
			return bd;
		} catch (InterruptedException ignored) {}
		return null;
	}

	public DataBatch getNextDataToBeProcessed(){
		try {
			return dataToProcessed.take();
		} catch (InterruptedException e) {
			//do nothing
		}
		return null;
	}

	public  void addProcessedData(DataBatch db){
		try {
			processedData.get(db.getGpu()).put(db);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getTrainedModels(){
		return stats.getTrainedModels();
	}

	public int getNumOfDataBatchProcessed(){
		return stats.getDataBatchProcessed();
	}

	public int getNumOfCpuTimeUnits(){
		return stats.getCPUTimeUnitUsed();
	}

	public int getNumOfGpuTimeUnits(){
		return stats.getGPUTimeUnitUsed();
	}
}
