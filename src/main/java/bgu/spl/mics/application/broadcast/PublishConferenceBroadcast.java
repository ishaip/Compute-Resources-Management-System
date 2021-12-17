package bgu.spl.mics.application.broadcast;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import javax.swing.text.AbstractDocument;
import java.util.ArrayList;
import java.util.LinkedList;

public class PublishConferenceBroadcast implements Broadcast {
    private final ArrayList<Model> models;


    public PublishConferenceBroadcast(ArrayList<Model> models){this.models = models;}

    public ArrayList<Model> getModel(){return models;}
}
