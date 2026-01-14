package JoaoDevFramework.entities;

import JoaoDevFramework.classes.JsonDeserializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HttpStatus status = HttpStatus.OK;
    private Map<Class<?>, String> contentTypes = new HashMap<>();
    private Map<String, String> responseHeaders = new HashMap<>(Map.of("Server", "Custom Server"));
    private JsonDeserializer jsonDeserializer = new JsonDeserializer();

    private String responseBody = "";

    public HttpResponse(Response response) {

        status = response.getHttpStatus();
        if(response.getBody() != null){
            if (response.getBody() instanceof String) {
                setResponseBody(response.getBody().toString());
            }else{
                setResponseBody(jsonDeserializer.deserializeObject(response.getBody()));
            }
        }



    }

    public void setResponseBody(String responseBody){
            this.responseBody = responseBody;
            responseHeaders.put("Content-Type", "application/json");
            Integer byteSize =  responseBody.getBytes(StandardCharsets.UTF_8).length;
            responseHeaders.put("Content-Length", String.valueOf(byteSize));
    }


    public String constructHttpResponse(){

        StringBuilder builder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        //Response Line
        builder.append(String.format(String.format("HTTP/1.0 %s %s\r\n", status.getStatusCode(), status)));

        //Headers
        responseHeaders.keySet().forEach((key) -> {
            builder.append(String.format("%s: %s\r\n", key, responseHeaders.get(key)));
        });
        builder.append("\r\n");

        //Body
        if(responseBody != null && !responseBody.isEmpty()){
            builder.append(responseBody);
        }

        return builder.toString();

    }


}
