package bgu.spl.mics.application.objects;

import java.util.LinkedList;

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
    private LinkedList<Model> models = new LinkedList<>();

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

    public void addModel(Model model){ models.addLast(model); }

    public LinkedList<Model> getModels(){ return models; }

    public String toString(){
        String output = "";

        output += "\"name\": \"" + name + "\",\n";
        output += "\"department\": \"" + department + "\",\n";
        output += "\"status\": \"" + status + "\",\n";
        output += "\"publications\": " + Integer.toString(publications) + ",\n";
        output += "\"papersRead\": " + Integer.toString(papersRead) + ",\n";
        output += "\"trainedModels\": [\n\t{\n\t\t";


        return output;
    }

}


