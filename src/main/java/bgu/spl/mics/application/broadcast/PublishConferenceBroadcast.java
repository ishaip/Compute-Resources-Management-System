package bgu.spl.mics.application.broadcast;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Student;

import javax.swing.text.AbstractDocument;

public class PublishConferenceBroadcast implements Broadcast {
    private final String[] results;

    private final Student[] publishers;

    public PublishConferenceBroadcast(String[] results, Student[] publishers){this.results = results; this.publishers = publishers;}

    public String[] getResults(){return results;}
    public Student[] getPublishers(){return publishers;}
}
