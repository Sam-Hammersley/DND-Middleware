package uk.ac.tees;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import uk.ac.tees.net.Connection;
import uk.ac.tees.net.message.Message;
import uk.ac.tees.net.message.MessageType;

public class ServerProcess implements Runnable {

    private final ServerSocket serverSocket;

    private final ExecutorService executor;
    
    public ServerProcess(ServerSocket serverSocket, ExecutorService executor) {
        this.serverSocket = serverSocket;
        this.executor = executor;
    }
    
    @Override
    public void run() {
        for (;;) {
            try (Socket socket = serverSocket.accept(); Connection connection = new Connection(socket)) {
            	
            	executor.submit(() -> {
                    while (true) {
                        Message message = connection.read();

                        if (message.getType() == MessageType.TERMINATION) {
                            break;
                        }

                        System.out.println(message);//do something with messages here
                    }
            	});
            } catch (IOException e) {
                throw new RuntimeException("Exception thrown when receiving messages", e);
            }
        }
    }

}