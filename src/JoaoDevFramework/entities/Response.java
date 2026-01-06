package JoaoDevFramework.entities;

public class Response{

    private Integer httpStatus;
    private Object body;

    public Response(HttpStatus httpStatus){
        this.httpStatus = httpStatus.getStatusCode();
    }

    public Response body(Object body){
        this.body = body;
        return this;

    }

    public static Response status(HttpStatus httpStatus){
       return new Response(httpStatus);
    }


    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
