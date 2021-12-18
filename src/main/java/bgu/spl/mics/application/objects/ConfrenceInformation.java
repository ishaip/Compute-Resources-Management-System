package bgu.spl.mics.application.objects;

import java.util.ArrayList;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private ArrayList<Model> publications = new ArrayList<>();

    public ConfrenceInformation(String name, int date){
        this.name = name;
        this.date = date;
    }

    public int getDate(){
        return date;
    }

    public String getName(){
        return name;
    }

    public void addPublication (Model model){
        publications.add(model);
    }

    public ArrayList<Model> getPublications (){
        return publications;
    }

    public String toString() {
        String str = "";

        str += "\t\"name\": \"" + name + "\",\n";
        str += "\t\t\t\"date\": " + Integer.toString(date) + ",\n";
        str += "\t\t\t\"publications\": [";

        if ( publications.size() > 0)
            str += "\n\t\t\t\t";
        for (int i = 0; i < publications.size(); i++){
            str += "{\n\t\t\t\t\t" + publications.get(i).toString() + "\n\t\t\t\t}";
            if ( i < publications.size() - 1 )
                str += ",";
        }
        if ( publications.size() > 0 )
            str += "\n\t\t\t";
        str += "]";
        return str;
    }
}








