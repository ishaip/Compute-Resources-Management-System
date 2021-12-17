package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {

        //--------------------File-Input-----------------------
        File input = new File("/users/studs/bsc/2022/picus/Desktop/jason_ishai_test"); //TODO: change pathname input

        //Lists of inputs objects
        ArrayList<Student> studentList = new ArrayList<>();
        ArrayList<StudentService> studentServiceList = new ArrayList<>();
        ArrayList<GPU> gpuList = new ArrayList<>();
        ArrayList<GPUService> gpuServiceList = new ArrayList<>();
        ArrayList<CPU> CPUList = new ArrayList<>();
        ArrayList<CPUService> cpuServiceList = new ArrayList<>();
        ArrayList<ConfrenceInformation> conferenceList = new ArrayList<>();
        int tickTime;
        int duration; //TODO: figure out whether it's int or long

        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();

            //extracting Students into array
            JsonArray JsonArrayOfStudents = fileObject.get("Students").getAsJsonArray();

            //process all students
            for (JsonElement e : JsonArrayOfStudents){
                //get the Json object
                JsonObject studentObject = e.getAsJsonObject();

                //extract the data of the current student
                String name = studentObject.get("name").getAsString();
                String department = studentObject.get("department").getAsString();
                String status = studentObject.get("status").getAsString();

                Student st = new Student(name, department, status);
                studentList.add(st);

                JsonArray JsonArrayOfModels = studentObject.get("models").getAsJsonArray();
                for (JsonElement m : JsonArrayOfModels){
                    //get the Json object
                    JsonObject modelObject = m.getAsJsonObject();

                    //extract the data
                    String modelName = modelObject.get("name").getAsString();
                    String type = modelObject.get("type").getAsString();
                    int size = modelObject.get("size").getAsInt();

                    Model model = new Model(modelName, type, size, st);
                    st.addModel(model);
                }
                studentServiceList.add(new StudentService(name, st));
            }

            //process all gpus
            JsonArray JsonArrayOfGPU = fileObject.get("GPUS").getAsJsonArray();
            int index = 0;
            for (JsonElement g : JsonArrayOfGPU){
                //JsonObject gpuObject = JsonArrayOfGPU.get(i).getAsJsonObject();
                //String t = gpuObject.getAsString();
                //JsonObject gpuObject = g.getAsJsonObject();
                //String str = gpuObject.getAsString();
                String str = g.getAsString();
                GPU gpu = new GPU(str);
                gpuList.add(gpu);

                String name = String.format("gpu_%d", index);
                gpuServiceList.add(new GPUService("name", gpu));
                index ++;
            }
            index = 0;
            //process all cpus
            JsonArray JsonArrayOfCPU = fileObject.get("CPUS").getAsJsonArray();
            for (JsonElement e : JsonArrayOfCPU){
                //JsonObject cpuObject = JsonArrayOfCPU.get(i).getAsJsonObject();
                int numOfCores = e.getAsInt();
                CPU cpu = new CPU(numOfCores);
                CPUList.add(cpu);

                String name = String.format("cpu_%d", index);
                cpuServiceList.add(new CPUService(name, cpu));
                index ++;
            }

            //process all conferences
            JsonArray JsonArrayOfConferences = fileObject.get("Conferences").getAsJsonArray();
            for (JsonElement e : JsonArrayOfConferences){
                JsonObject conferenceObject = e.getAsJsonObject();

                //extract the data of the conference
                String name = conferenceObject.get("name").getAsString();
                int date = conferenceObject.get("date").getAsInt();

                conferenceList.add(new ConfrenceInformation(name, date));
            }

            tickTime = fileObject.get("TickTime").getAsInt();
            duration = fileObject.get("Duration").getAsInt();

            TimeService timeService = new TimeService("timeService",tickTime,duration);
            new Thread(timeService).start();


            for (StudentService s: studentServiceList)
                new Thread(s).start();
            for (ConfrenceInformation cl : conferenceList ){
                ConferenceService cs = new ConferenceService(cl.getName(),cl);
                new Thread(cs).start();
            }
            for (GPUService g : gpuServiceList)
                new Thread(g).start();
            for (CPUService c : cpuServiceList)
                new Thread(c).start();




        } catch (Exception e) {
            e.printStackTrace();
        }
        //--------------------File-output-----------------------
    }

}
