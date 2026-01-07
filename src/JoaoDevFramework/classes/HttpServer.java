package JoaoDevFramework.classes;

import JoaoDevFramework.entities.*;

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

    public HttpServer(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public void connect(Socket cliente) throws IOException {

        //Tem que ser refatorado pra InputStream e ler só por byte no futuro


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
        ) {
            try{
                HttpRequest httpRequest = parseHttpRequest(reader);

                Response response = httpHandler.handlerFinder(httpRequest);

                HttpResponse httpResponse = new HttpResponse(response);

                out.write(httpResponse.constructHttpResponse());
            } catch (Exception e) {
                System.out.println(e);
                Response error = Response.status(HttpStatus.INTERNAL_SERVER_ERROR);
                HttpResponse httpResponse = new HttpResponse(error);
                out.write(httpResponse.constructHttpResponse());
            }


        }
    }

    private HttpRequest parseHttpRequest(BufferedReader reader) throws IOException {
        boolean requestLineRead = false;
        String line = "";  HttpRequest httpRequest = new HttpRequest();
        Map<String, String> rawHeaders = new HashMap<>();

        while ((line = reader.readLine()) != null) {

            if (!requestLineRead) {

                //Empty Request
                if(line.isEmpty()) break;;

                String[] httpArguments = line.split(" ");

                httpRequest.setHttpMethod(HttpMethod.valueOf(httpArguments[0]));
                httpRequest.setPath(httpArguments[1]);
                httpRequest.setHttpVersion(httpArguments[2]);

                requestLineRead = true;
                continue;

            }

            if (line.isEmpty()) {

                if (httpRequest.getHttpMethod() == HttpMethod.GET) break;

                int contentLength = httpRequest.getContentLength().intValue();

                if (!(contentLength > 0)) break;

                //Body reading
                char[] charBuffer = new char[contentLength];

                StringBuilder stringBuilder = new StringBuilder();
                int totalRead = 0;
                while (totalRead < contentLength) {
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

        return httpRequest;
    }

}
