package homework;


import io.grpc.ServerBuilder;
import homework.service.CounterService;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var remoteService = new CounterService();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteService).build();
        server.start();
        System.out.println("server is waiting for client connections...");
        server.awaitTermination();
    }
}
