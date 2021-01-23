public class Response<Status, Data> {
    public Status status;
    public Data data;

    public Response(){}

    public Response(Status status, Data data){
        this.status = status;
        this.data = data;
    }
}