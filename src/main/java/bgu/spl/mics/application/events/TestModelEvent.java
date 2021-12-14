package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TestModelEvent implements Event<Boolean> {

    //---------------------Fields----------------------
    private Future<Model.Result> future;
    private Model model;

    //-----------------Constructor---------------------
    public TestModelEvent (Model _model){
        this.future = new Future<Model.Result>();
        this.model = _model;
    }

    //-------------------Methods----------------------
    public Model.Result getResult(){
        return model.getResult();
    }

    public void setResult (Model.Result res){
        model.setResult(res);
    }

    public Future<Model.Result> getFuture(){return future;}

    public Model.Result test (){return future.get();}
}
