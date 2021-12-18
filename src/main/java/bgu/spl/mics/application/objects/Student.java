package bgu.spl.mics.application.objects;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    public enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications = 0;
    private int papersRead = 0;
    private ConcurrentLinkedQueue<Model> models = new ConcurrentLinkedQueue<>();
    //private ArrayList<Model> models = new ArrayList<>();

    public Student(String name,String department,Degree status, int publications,int papersRead ){
        this.name = name;
        this.department = department;
        this.papersRead = papersRead;
        this.status = status;
        this.publications = publications;
    }

    public Student(String name, String department, String degree){
        this.name = name;
        this.department = department;
        if (degree.equals("MSc"))
            this.status = Degree.MSc;
        else
            this.status = Degree.PhD;
    }

    public void readPaper(){ papersRead++; }

    public int getPapersRead(){ return papersRead; }

    public void addPublishResult(){ publications++; }

    public void publishPaper(){ publications++; }

    public Degree getDegree(){ return status; }

    public void addModel(Model model){ models.add(model); }

    //public LinkedList<Model> getModels(){ return models; }

    public ConcurrentLinkedQueue<Model> getModels(){ return models; }

    public String toString(){
        String output = "";

        output += "\t\"name\": \"" + name + "\",\n";
        output += "\t\t\t\"department\": \"" + department + "\",\n";
        output += "\t\t\t\"status\": \"" + status + "\",\n";
        output += "\t\t\t\"publications\": " + Integer.toString(publications) + ",\n";
        output += "\t\t\t\"papersRead\": " + Integer.toString(papersRead) + ",\n";
        output += "\t\t\t\"trainedModels\": [";

        Model[] mm = new Model[models.size()];
        int index = 0;
        while ( !models.isEmpty() ) {
            mm[index] = models.poll();
            index ++;
        }
        boolean isFirst = true;
        for (int i = 0; i < mm.length; i++){
            if ( mm[i].getStatus() != Model.Status.Trained && mm[i].getStatus() != Model.Status.Tested )
                continue;
            if ( isFirst ){
                output += "\n\t\t\t\t";
                isFirst = false;
            }
            output += "{\n\t\t\t";
            output += "\t\t" + mm[i].toString() + "\n\t\t\t\t}";
            output += ",\n\t\t\t\t";
            if ( i == mm.length - 1) {
                output = output.substring(0, output.length() - 6);
                output += "\n\t\t\t";
            }
        }
        output += "]";
        return output;
    }

}

//        if (models.size() > 0){
//            Iterator<Model> itr = models.iterator();
//            Model m;
//            while (itr.hasNext()){
//                m = itr.next();
//                if ( m.getStatus() != Model.Status.Trained || m.getStatus() != Model.Status.Tested )
//                    continue;
//
//                output += "{\n\t\t\t";
//                output += "\t\t" + m.toString() + "\n\t\t\t\t}";
//                output += ",\n\t\t\t\t";
//            }
//            output = output.substring(0, output.length() - 6);
//            output += "\n";
//        }

