package execeptions;

public class HttpRequestInvalid extends RuntimeException {
    public HttpRequestInvalid(String message) {
        super(message);
    }
}
