package bgu.spl.mics.application.events;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class TestModelEvent implements Event<Boolean> {

    //---------------------Fields----------------------
    private final Future<Model.Result> future;
    private final Model model;
    private final Student student;

    //-----------------Constructor---------------------
    public TestModelEvent (Model _model, Student student){
        this.future = new Future<>();
        this.model = _model;
        this.student = student;
    }

    //-------------------Methods----------------------
    public Model.Result getResult(){
        return model.getResult();
    }

    public void setResult (Model.Result res){
        model.setResult(res);
    }

    public void setStatus(Model.Status stat){model.setStatus(stat);}

    public Future<Model.Result> getFuture(){return future;}

    public Student getStudent(){return student;}
}
