package homework;

public class ThreadStarter {
    private static final int threadCount = 2;
    private static final int counterMaxValue = 10;
    private static final int counterMinValue = 1;

    public boolean start() {
        //начальные состояния потоков = очередность
        boolean[] signals = new boolean[threadCount];
        //первый поток стартует первым
        for (int i = 0; i < signals.length; i++) {
            signals[i] = (i == 0);
        }

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threads.length; i++) {
            final int index = i;
            threads[i] = new Thread(() -> this.action(signals, index));
            threads[i].start();
        }
        return true;
    }

    private void action(boolean[] signalsMonitor, int index) {
        try {
            int counterValue = counterMinValue;
            int i = 1;
            int iterationCount = counterMaxValue * 2;

            while (i < iterationCount) {
                boolean continueWaiting = (i < iterationCount - 1);
                //захват монитора
                synchronized (signalsMonitor) {
                    //проверка флага очередности
                    if (signalsMonitor[index]) {
                        //System.out.println(i + ", " + counterValue + ", " + index + ", " + (i > counterMaxValue));
                        System.out.print(counterValue);
                        i++;
                        if (i > counterMaxValue)
                            counterValue--;
                        else
                            counterValue++;
                        //сброс своего флага
                        signalsMonitor[index] = false;
                        int indexNext = index < signalsMonitor.length - 1 ? index + 1 : 0;
                        //установка флага для следующей нитки
                        signalsMonitor[indexNext] = true;
                        //уведомление соседей
                        signalsMonitor.notifyAll();
                    }
                    if (continueWaiting)
                        signalsMonitor.wait();
                }
            }
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
