package JoaoDevFramework.classes;

import JoaoDevFramework.Controller2;
import JoaoDevFramework.TestController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public void initServer(){
        Set<Object> objects = new HashSet<>();
        objects.add(new TestController());
        objects.add(new Controller2());
        Starter starter = new Starter(objects);
        HttpHandlerFinder httpHandlerFinder = new HttpHandlerFinder(starter.getAppMap(), new JsonSerializer());

        HttpConnection httpConnection = new HttpConnection(httpHandlerFinder);
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor(); ServerSocket serverSocket = new ServerSocket(8080)) {

            while (true) {
                Socket client = serverSocket.accept();

                executor.submit(() -> {
                    try {
                        httpConnection.connect(client);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (!client.isClosed()) {
                            try {
                                client.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }


                });

            }


        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

}
