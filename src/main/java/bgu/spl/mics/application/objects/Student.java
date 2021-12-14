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
    enum Degree {
        MSc, PhD
    }

    private int name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead = 0;
    private LinkedList<Model> models;

    public Student(int name,String department,Degree status, int publications,int papersRead ){
        this.name = name;
        this.department = department;
        this.papersRead = papersRead;
        this.status = status;
        this.publications = publications;
    }

    public void readPaper(){papersRead++;}

    public void setPapersRead(int papersRead){this.papersRead = papersRead;}

    public int getPapersRead(){return papersRead;}

    public void addPublishResult(){publications++;}

    public Degree getDegree(){return status;}

    public void addModel(Model model){models.addLast(model);}

    public LinkedList<Model> getModels(){return models;}

}


