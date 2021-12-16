package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class PublishResultEvent implements Event<Boolean> {

    private Model model;

    public PublishResultEvent(Model model){
        this.model = model;
    }

    public Model getModel(){return model;}
}
