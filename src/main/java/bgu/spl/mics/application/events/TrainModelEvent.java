package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Boolean> {
    private Model model;

    public TrainModelEvent(Model model){ this.model = model;}

    public Data getData(){return model.getData();}

    public void setStatus(Model.Status status){model.setStatus(status);}




}
