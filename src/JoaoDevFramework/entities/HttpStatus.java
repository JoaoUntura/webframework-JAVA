package JoaoDevFramework.entities;

public enum HttpStatus {

    OK(200),
    CREATED(201);

    private final Integer status;

    HttpStatus(Integer status){
        this.status = status;
    }

    public Integer getStatusCode(){
        return this.status;
    }


}
