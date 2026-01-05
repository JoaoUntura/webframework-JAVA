package JoaoDevFramework.classes;

import JoaoDevFramework.entities.HttpMethod;
import JoaoDevFramework.entities.HttpRequest;
import JoaoDevFramework.entities.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpServer {

    private final HttpHandler httpHandler;

    public HttpServer(HttpHandler httpHandler){
        this.httpHandler = httpHandler;
    }

    public void connect(Socket cliente){

        //Tem que ser refatorado pra InputStream e ler só por byte no futuro
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()))
        ) {

            HttpRequest httpRequest = new HttpRequest();
            Map<String, String> rawHeaders = new HashMap<>();
            int lineCounter = 0;
            String line = "";

            while ((line = reader.readLine()) != null ) {

                //Empty Request
                if(line.isEmpty() && lineCounter == 0) break;

                if(lineCounter == 0){

                    String[] httpArguments = line.split(" ");

                    httpRequest.setHttpMethod(HttpMethod.valueOf(httpArguments[0]));
                    httpRequest.setPath(httpArguments[1]);
                    httpRequest.setHttpVersion(httpArguments[2]);
                    lineCounter ++;
                    continue;

                }

                if(line.isEmpty()) {

                    if(httpRequest.getHttpMethod() == HttpMethod.GET) break;

                    int contentLength = httpRequest.getContentLength().intValue();

                    if (!(contentLength > 0)) break;

                    //Body reading

                    char[] charBuffer = new char[contentLength];

                    StringBuilder stringBuilder = new StringBuilder();
                    int totalRead = 0;
                    while (totalRead < contentLength){
                        int read = reader.read(charBuffer);
                        if (read == -1) break;
                        String newChars = new String(charBuffer, 0, read);
                        stringBuilder.append(newChars);
                        totalRead += newChars.getBytes(StandardCharsets.UTF_8).length;


                    }

                    String body = stringBuilder.toString();

                    httpRequest.setBody(body);
                    break;

                }


                String[] headerValue = line.split(": ");

                String key = headerValue[0];
                String value = headerValue[1];

                httpRequest.setHttpRequestHeader(key, value);

                rawHeaders.put(key, value);

            }

            httpRequest.setRawHeaders(rawHeaders);

            Optional<Object> response = httpHandler.handlerFinder(httpRequest);

            LocalDateTime now = LocalDateTime.now();

            if (response.isPresent() && response.get() instanceof  Response){
                Response responseObject = (Response) response.get();

                out.write(String.format("HTTP/1.0 %s OK\r\n", responseObject.getHttpStatus()));
                out.write("Date: " + now + "\r\n");
                out.write("Server: Custom Server\r\n");
                out.write("Content-Type: text/plain\r\n");
                out.write("Content-Length: " + 2 + "\r\n");
                out.write("\r\n");
                out.write("Ok");



            }else {

                out.write("HTTP/1.0 200 OK\r\n");
                out.write("Date: " + now + "\r\n");
                out.write("Server: Custom Server\r\n");
                out.write("\r\n");

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
