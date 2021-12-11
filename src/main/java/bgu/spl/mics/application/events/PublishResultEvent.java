package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class PublishResultEvent implements Event<Boolean> {
    private Future<Boolean> published;

    public PublishResultEvent(Future<Boolean> published){
        this.published = published;
    }

    public Future<Boolean> getPublishedIsPreformed(){
        return published;
    }
}
