package homework.handlers;

import homework.generated.CounterResponse;

public class CounterWrapper {
    private long counter = 0;
    private boolean wasUsed = false;

    public CounterWrapper(CounterResponse response) {
        this.counter = response.getCounter();
    }

    public long GetCounter() {
        return counter;
    }

    public boolean GetWasUsed() {
        return wasUsed;
    }

    public void SetWasUsed(boolean wasUsed) {
        this.wasUsed = wasUsed;
    }
}
