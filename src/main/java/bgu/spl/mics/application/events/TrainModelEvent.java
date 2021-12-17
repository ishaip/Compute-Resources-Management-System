package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Boolean> {
    private final Model model;
    private Future <Model.Status> testModelIsPreformed;

    public TrainModelEvent(Model model){
        this.model = model;
        testModelIsPreformed = new Future<Model.Status>();
    }

    public Data getData(){return model.getData();}

    public void setStatus(Model.Status status){model.setStatus(status);}

    public Model.Status getStatus(){return model.getStatus();}

    public Future<Model.Status> getFuture(){return testModelIsPreformed;}

}
