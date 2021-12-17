package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.broadcast.PublishConferenceBroadcast;
import bgu.spl.mics.application.broadcast.TerminateBroadcast;
import bgu.spl.mics.application.events.PublishResultEvent;
import bgu.spl.mics.application.events.TestModelEvent;
import bgu.spl.mics.application.events.TrainModelEvent;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.concurrent.TimeUnit;

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
    private boolean terminated = false;
    private Future<Model.Result> testModelFuture;
    private Future<Model.Status> trainModelFuture;

    public StudentService(String name, Student student) {
        super(name);
        this.student=student;
    }

    private void waitForResults()  {
        while (!terminated) {
            for (Model m : student.getModels()) {
                TrainModelEvent trainModelEvent = new TrainModelEvent(m);
                trainModelFuture = trainModelEvent.getFuture();
                trainModelFuture.get();
                //wait until training is done
                TestModelEvent testModelEvent = new TestModelEvent(m, student);
                testModelFuture = testModelEvent.getFuture();
                sendEvent(testModelEvent);
                Model.Result result = testModelFuture.get();
                //wait until testing is done
                if (result == Model.Result.Good)
                    sendEvent(new PublishResultEvent(m));
            }
        }
    }

    @Override
    protected void initialize() {
        Thread runResults = new Thread(this::waitForResults);
        runResults.start();

        subscribeBroadcast(PublishConferenceBroadcast.class, c -> {
            for (Model m : c.getModel()) {
                Student s = m.getStudent();
                if (s != student)
                    student.readPaper();
                else
                    student.publishPaper();
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, c -> {
            terminated = true;
            runResults.interrupt();
            terminate();
        });


    }
}

