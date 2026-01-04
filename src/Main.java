import JoaoDevFramework.Starter;
import JoaoDevFramework.TestController;
import entities.HttpMethod;
import entities.HttpRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Set<Object> objects = new HashSet<>();
        objects.add(new TestController());

        Starter starter = new Starter(objects);
        System.out.println(starter.toString());

       try(ServerSocket serverSocket = new ServerSocket(8080)){
            while(true) {
                try (Socket cliente = serverSocket.accept()) {

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

                                if (httpRequest.getContentLength() == null || httpRequest.getContentLength().equals(0L)) break;

                                //Body reading

                                char[] charBuffer = new char[httpRequest.getContentLength().intValue()];
                                int totalCharsRead = 0;
                                while (totalCharsRead < httpRequest.getContentLength().intValue()){
                                    int read = reader.read(charBuffer);
                                    if (read == -1) break;
                                    totalCharsRead += read;
                                }

                                String body = String.copyValueOf(charBuffer);

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

                        Optional<Object> response = starter.handlerFinder(httpRequest);

                        LocalDateTime now = LocalDateTime.now();

                        if (response.isPresent()){

                            Object responseFinal = response.get();

                            String reponseString = responseFinal.toString();

                            out.write("HTTP/1.0 200 OK\r\n");
                            out.write("Date: " + now + "\r\n");
                            out.write("Server: Custom Server\r\n");
                            out.write("Content-Type: text/plain\r\n");
                            out.write("Content-Length: " + reponseString.length() + "\r\n");
                            out.write("\r\n");
                            out.write(reponseString);
                        }else {

                            out.write("HTTP/1.0 200 OK\r\n");
                            out.write("Date: " + now + "\r\n");
                            out.write("Server: Custom Server\r\n");
                            out.write("\r\n");

                        }








                    }

                }
            }

       }

    }
}