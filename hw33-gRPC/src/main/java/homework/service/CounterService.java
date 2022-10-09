package homework.service;

import homework.generated.*;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class CounterService extends CounterServiceGrpc.CounterServiceImplBase {
    private final long defaultPause = 2000;
    public CounterService() {
    }

    public void getCounter(CounterRequest request, StreamObserver<CounterResponse> responseObserver) {
        long counter = request.getIndexFrom();
        long pause = (request.getPause() > 0 ? request.getPause() : defaultPause);
        while (counter <= request.getIndexTo()) {
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(createCounterResponse(counter));
            System.out.println("Counter sent, value=" + counter);
            counter++;
        }

        responseObserver.onCompleted();
    }

    private CounterResponse createCounterResponse(long counter) {
        return CounterResponse.newBuilder()
                .setCounter(counter)
                .build();
    }
}
