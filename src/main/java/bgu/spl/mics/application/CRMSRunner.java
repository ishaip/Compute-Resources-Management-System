package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.io.File;
import java.io.FileWriter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static CountDownLatch threadInitCounter;
    public static AtomicInteger cpuTimeUsed = new AtomicInteger(0);
    public static AtomicInteger gpuTimeUsed = new AtomicInteger(0);
    public static AtomicInteger batchesProcessed = new AtomicInteger(0);
    public static void main(String[] args) {

        //--------------------File-Input-----------------------
        //File input = new File("/home/spl211/IdeaProjects/SPL_Assignment_2_v1/example_input.json");
//        File input = new File("/users/studs/bsc/2022/picus/IdeaProjects/SPL_2021_Assignment_2/example_input.json");
        File input = new File(args[0]); //TODO: fix program argument in configuration

        //Lists of inputs objects
        ArrayList<Student> studentList = new ArrayList<>();
        ArrayList<StudentService> studentServiceList = new ArrayList<>();
        ArrayList<GPU> gpuList = new ArrayList<>();
        ArrayList<GPUService> gpuServiceList = new ArrayList<>();
        ArrayList<CPU> CPUList = new ArrayList<>();
        ArrayList<CPUService> cpuServiceList = new ArrayList<>();
        ArrayList<ConfrenceInformation> conferenceList = new ArrayList<>();
        int tickTime = 0;
        int duration = 0;

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
            for (JsonElement e : JsonArrayOfGPU){
                String str = e.getAsString();
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

        } catch (FileNotFoundException e) {
            e.printStackTrace(); //or maybe do nothing?
        }

        //------------------Program-Execution--------------------
        threadInitCounter = new CountDownLatch(gpuServiceList.size() + conferenceList.size());

        //initialize the Threads
        TimeService timeService = new TimeService("timeService",tickTime,duration);
        Thread timeServiceThread = new Thread(timeService);
        timeServiceThread.start();

        ArrayList<Thread> conferenceThreads = new ArrayList<>();
        for (ConfrenceInformation cl : conferenceList ){
            ConferenceService cs = new ConferenceService(cl.getName(),cl);
            Thread cst = new Thread(cs);
            conferenceThreads.add(cst);
            cst.start();
        }

        ArrayList<Thread> gpuThreads = new ArrayList<>();
        for (GPUService g : gpuServiceList){
            Thread gt = new Thread(g);
            gpuThreads.add(gt);
            gt.start();
        }

        ArrayList<Thread> cpuThreads = new ArrayList<>();
        for (CPUService c : cpuServiceList){
            Thread ct = new Thread(c);
            cpuThreads.add(ct);
            ct.start();
        }

        //join threads
//        for (int i = 0; i < conferenceThreads.size(); i++) {
//            try {
//                conferenceThreads.get(i).join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < gpuThreads.size(); i++) {
//            try {
//                gpuThreads.get(i).join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < cpuThreads.size(); i++) {
//            try {
//                cpuThreads.get(i).join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            timeServiceThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try{
            threadInitCounter.await(); //wait for all services to register
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Thread> studentsThreads = new ArrayList<>();
        for (StudentService s: studentServiceList){
            Thread st = new Thread(s);
            studentsThreads.add(st);
            st.start();
        }

        for (int i = 0; i < studentsThreads.size(); i++) {
            try {
                studentsThreads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            t.interrupt();
        }

        //--------------------File-output-----------------------
        File output = new File("/home/spl211/IdeaProjects/SPL_Assignment_2_v1/output_try.txt");
//        File output = new File("/users/studs/bsc/2022/picus/IdeaProjects/SPL_2021_Assignment_2/output_try.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(output);

            //writing the students into the output file
            writer.write("{\n\t\"students\": [");
            for (int i = 0; i < studentList.size(); i++) {
                writer.write("\n\t\t{\n\t\t");
                writer.write(studentList.get(i).toString());
                writer.write("\n\t\t}");
                if ( i < studentList.size() - 1 )
                    writer.write(",");
            }
            writer.write("\n\t],\n");

            //writing the conferences into the output file
            writer.write("\t\"conferences\": [\n");
            for (int i = 0; i < conferenceList.size(); i++){
                writer.write("\t\t{\n\t\t");
                writer.write(conferenceList.get(i).toString());
                writer.write("\n\t\t}");
                if ( i < conferenceList.size() - 1 )
                    writer.write(",");
                writer.write("\n");
            }
            writer.write("\t],\n");

            //writing the entire data
            writer.write("\t\"cpuTimeUsed\": ");
            writer.write(Integer.toString(cpuTimeUsed.get()));
            writer.write(",\n");

            writer.write("\t\"gpuTimeUsed\": ");
            writer.write(Integer.toString(gpuTimeUsed.get()));
            writer.write(",\n");

            writer.write("\t\"batchesProcessed\": ");
            writer.write(Integer.toString(batchesProcessed.get()));
            writer.write("\n");

            writer.write("}");

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
