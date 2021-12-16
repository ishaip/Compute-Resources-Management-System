package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileDescriptor;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        File input = new File("c:/data.json"); //TODO: change pathname input
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();

            //extracting Students into array
            JsonArray JsonArrayOfStudents = fileObject.get("Student").getAsJsonArray();
            //process all students
            for (JsonElement e : JsonArrayOfStudents){
                //get the Json object
                JsonObject studentObject = e.getAsJsonObject();

                //extract the data
                String name = studentObject.get("name").getAsString();
                String department = studentObject.get("department").getAsString();
                String status = studentObject.get("status").getAsString(); // should be cast to 'Degree'
                int publications = studentObject.get("").getAsInt();
            }


            //extraction the basic fields
//            String fieldName1 = fileObject.get("fieldName1").getAsString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        System.out.println("Hello World!");
    }
}

/*
    public Student(String name, String department, String degree, Model model){
        this.name = name;
        this.department = department;
        if (degree.equals("MSc"))
            this.status = Degree.MSc;
        else
            this.status = Degree.PhD;
        this.publications = 0;
        this.papersRead = 0;
        this.models.add(model);
    }
 */

/*
    public Student(String name, String department, String degree, Model model){
        this.name = name;
        this.department = department;
        if (degree.equals("MSc"))
            this.status = Degree.MSc;
        else
            this.status = Degree.PhD;
        this.publications = 0;
        this.papersRead = 0;
        this.models.add(model);
    }
 */