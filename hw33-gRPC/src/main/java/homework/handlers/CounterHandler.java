package homework.handlers;

import homework.generated.CounterRequest;
import homework.generated.CounterResponse;
import homework.generated.Empty;
import homework.generated.CounterServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Stack;
import java.util.concurrent.CountDownLatch;

public class CounterHandler {
    private String serverHost = "localhost";
    private int serverPort = 8190;
    private int pauseDefault = 1000;

    public CounterHandler(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void handle(long indexFrom, long indexTo, long pauseServer, long pauseClient, int iterationCount) throws InterruptedException {
        //хранилище счетчиков с сервера
        Stack<CounterWrapper> counterStack = new Stack();


        CounterRequest request = CounterRequest.newBuilder()
                        .setIndexFrom(indexFrom)
                        .setIndexTo(indexTo)
                        .setPause(pauseServer)
                        .build();

        ManagedChannel channel =  getChannel();
        System.out.println("stream started");
        var latch = new CountDownLatch(1);
        var stub = CounterServiceGrpc.newStub(channel);

        //запускаем нитку логирования
        Thread logger = new Thread(() -> this.logCounter(counterStack, pauseClient, iterationCount));
        logger.start();

        stub.getCounter(request, new StreamObserver<CounterResponse>() {
            @Override
            public void onNext(CounterResponse response) {
                saveCounter(counterStack, response);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("stream finished");
                latch.countDown();
            }
        });

        latch.await();

        channel.shutdown();

    }

    private ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(serverHost, serverPort).usePlaintext().build();
    }

    private void logCounter(Stack<CounterWrapper> counterStack, long pauseClient, int iterationCount) {
        long currentValue = 0;

        long pause = (pauseClient > 0 ? pauseClient : pauseDefault);
        for (int i = 0; i < iterationCount; i++) {
            try {
                Thread.sleep(pause);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (counterStack) {
                if (!counterStack.empty()) {
                    CounterWrapper lastCounter = counterStack.peek();
                    if (!lastCounter.GetWasUsed()) {
                        currentValue += lastCounter.GetCounter();
                        lastCounter.SetWasUsed(true);
                    }
                }
            }
            currentValue++;
            System.out.println("currentValue: " + currentValue);
        }
    }

    private void saveCounter(Stack<CounterWrapper> counterStack, CounterResponse response) {
        System.out.println("new value: " + response.getCounter());

        synchronized (counterStack) {
            counterStack.push(new CounterWrapper(response));
        }
    }
}
