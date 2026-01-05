import JoaoDevFramework.Starter;
import JoaoDevFramework.TestController;
import JoaoDevFramework.classes.HttpHandler;
import JoaoDevFramework.classes.HttpServer;
import JoaoDevFramework.entities.HttpMethod;
import JoaoDevFramework.entities.HttpRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Set<Object> objects = new HashSet<>();
        objects.add(new TestController());
        Starter starter = new Starter(objects);
        HttpHandler httpHandler = new HttpHandler(starter.getAppMap());

        HttpServer httpServer = new HttpServer(httpHandler);
        try(ServerSocket serverSocket = new ServerSocket(8080)){
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
                while(true) {
                    Socket cliente = serverSocket.accept();

                    executor.submit(() -> {
                        try{
                            httpServer.connect(cliente);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }finally {
                            if(!cliente.isClosed()){
                                try {
                                    cliente.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }


                    });

                }


        }




    }
}