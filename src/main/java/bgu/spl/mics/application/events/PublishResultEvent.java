package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Student;

public class PublishResultEvent implements Event<Boolean> {
    private Student publisher;

    public PublishResultEvent( Student publisher){
        this.publisher = publisher;
    }

    //public Future<Boolean> getPublishedIsPreformed(){
      //  return published;
    //}
}
