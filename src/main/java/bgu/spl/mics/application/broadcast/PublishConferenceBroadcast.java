package bgu.spl.mics.application.broadcast;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import javax.swing.text.AbstractDocument;
import java.util.LinkedList;

public class PublishConferenceBroadcast implements Broadcast {
    private final LinkedList<Model> models;


    public PublishConferenceBroadcast(LinkedList<Model> models){this.models = models;}

    public LinkedList<Model> getModel(){return models;}
}
