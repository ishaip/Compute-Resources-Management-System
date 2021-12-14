package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.PublishConferenceBroadcast;
import bgu.spl.mics.application.events.TestModelEvent;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.LinkedList;

/**
 * Student is responsible for sending the {@link //TrainModelEvent},
 * {@link //TestModelEvent} and {@link //PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
    private final Student student;
    private MessageBusImpl ms = MessageBusImpl.getInstance();

    public StudentService(String name, Student student) {
        super(name);
        this.student=student;
    }


    @Override
    protected void initialize() {
        subscribeBroadcast(PublishConferenceBroadcast.class, c -> {
            student.setPapersRead(student.getPapersRead() + c.getResults().length);
        });
        //TODO remember to remove the papers the studnet himself published
        LinkedList<Future<Model.Result>> testModelsFutures = new LinkedList<Future<Model.Result>>();
        for (Model m: student.getModels() ){
            TestModelEvent mEvent = new TestModelEvent(m);
            testModelsFutures.add(mEvent.getFuture());
            sendEvent(mEvent);
        }
        //TODO check the futures for good results
    }
}

