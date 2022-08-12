package homework;

import io.grpc.ManagedChannelBuilder;
import homework.generated.Empty;
import homework.handlers.CounterHandler;

import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    public static void main(String[] args) throws InterruptedException {
        CounterHandler handler = new CounterHandler("localhost", 8190);
        handler.handle(0, 30, 2000, 1000, 50);
    }
}
