package bgu.spl.mics.application.broadcast;

import bgu.spl.mics.Broadcast;

import javax.swing.text.AbstractDocument;

public class PublishConferenceBroadcast implements Broadcast {
    private final String[] results;

    public PublishConferenceBroadcast(String[] results){this.results = results;}

    public String[] getResults(){return results;}
}
