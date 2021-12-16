package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class PublishResultEvent implements Event<Boolean> {
    private Future<Boolean> published;
    private String name;

    public PublishResultEvent(Future<Boolean> published, String name){
        //this.published = published; this
    }

    public Future<Boolean> getPublishedIsPreformed(){
        return published;
    }
}
