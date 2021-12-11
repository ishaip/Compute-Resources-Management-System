package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TestModelEvent implements Event<String> {

    //---------------------Fields----------------------
    private Future<String> future;
    private Model model;

    //-----------------Constructor---------------------
    public TestModelEvent (){
        this.future = new Future<>();
    }

    //-------------------Methods----------------------
    public Result getResult(){
        return model.getResult();
    }
}
