package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class TestModelEvent implements Event<Boolean> {

    //---------------------Fields----------------------
    private Future<Model.Result> future;
    private final Model model;
    private final Student publisher;

    //-----------------Constructor---------------------
    public TestModelEvent (Model _model, Student publisher){
        this.future = new Future<Model.Result>();
        this.model = _model;
        this.publisher = publisher;

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

    public Student getPublisher(){return publisher;}
}
