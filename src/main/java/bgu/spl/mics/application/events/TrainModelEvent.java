package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Status;

public class TrainModelEvent implements Event<Boolean> {
    private Model model;

    public TrainModelEvent(Model model){ this.model = model;}

    public Data getData(){return model.getData();}

    public Status getStatus(){return model.getStatus();}



}
