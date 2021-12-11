package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Boolean> {
    private final Model model;
    private Future <Boolean> TestModelIsPreformed;

    public TrainModelEvent(Model model){ this.model = model;}

    public Data getData(){return model.getData();}

    public void setStatus(Model.Status status){model.setStatus(status);}

    public Model.Result getResult(){return model.getResult();}

    public Future<Boolean> getTestModelIsPreformed(){return TestModelIsPreformed;}
}
