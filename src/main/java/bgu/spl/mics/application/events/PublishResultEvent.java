package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class PublishResultEvent implements Event<Boolean> {
    private Student publisher;
    private Model model;

    public PublishResultEvent( Student publisher,Model model){
        this.publisher = publisher;
        this.model = model;
    }

    public Student getPublisher(){return publisher;}

    public Model getModel(){return model;}
}
