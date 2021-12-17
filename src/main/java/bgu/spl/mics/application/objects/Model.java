package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class Model {

    public enum Status {
        PreTrained, Training, Trained, Tested
    }

    public enum Result {
        None, Good, Bad
    }

    private String name;
    private Data data;
    private Student student;
    private Status status;
    private Result result;

    public Model(String name,Data data, Student student, Status status, Result result){
        this.name = name;
        this.data = data;
        this.status = status;
        this.student = student;
        this.result = result;
    }

    public Model (String name, String dataType, int sizeOfData, Student student){
        this.name = name;
        this.student = student;
        this.status = Status.PreTrained;
        this.result = Result.None;

        //init the data
        Data.Type type;
        if (dataType.equals("Images"))
            type = Data.Type.Images;
        else if (dataType.equals("Tabular"))
            type = Data.Type.Tabular;
        else
            type = Data.Type.Text;
        this.data = new Data(type, sizeOfData);
    }

    public void setResult(Result result){ this.result = result; }

    public  Result getResult() { return result; }

    public void setStatus(Status status){ this.status = status; }

    public Data getData() { return data; }

    public Status getStatus(){
        return status;
    }

    public Student getStudent(){ return student; }
}
