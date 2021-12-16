package bgu.spl.mics.application.objects;
import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayList;


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
	private static volatile boolean  isDone = false;
	private ArrayList<GPU> gpus;
	private ArrayList<CPU> cpus;
	private Statistics stats;
	private static Cluster instance = null;

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
	public ArrayList<CPU> getCpus() {
		return cpus;
	}

	public ArrayList<GPU> getGpus() {
		return gpus;
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
