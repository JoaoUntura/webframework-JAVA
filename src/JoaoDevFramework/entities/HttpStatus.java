package JoaoDevFramework.entities;

public enum HttpStatus {

    OK(200),
    CREATED(201),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final Integer status;

    HttpStatus(Integer status){
        this.status = status;
    }

    public Integer getStatusCode(){
        return this.status;
    }


}
