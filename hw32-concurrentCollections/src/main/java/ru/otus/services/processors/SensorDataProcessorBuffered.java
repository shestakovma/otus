package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    //выбрали ConcurrentSkipListSet, т.к. там есть встроенная сортировка
    private final ConcurrentSkipListSet<SensorData> dataBuffer = new ConcurrentSkipListSet(new Comparator<SensorData>() {
        @Override
        public int compare(SensorData o1, SensorData o2) {
            return o1.getMeasurementTime().compareTo(o2.getMeasurementTime());
        }
    });
    //собственный счетчик для размера буфера
    private int dataBufferCurrentCount = 0;
    //RWL для разграничения add и flush - когда работает flush, add стоят на блокировке
    ReentrantReadWriteLock dataBufferLock = new ReentrantReadWriteLock();
    Lock dataBufferLockAdd = dataBufferLock.readLock();
    Lock dataBufferLockFlush = dataBufferLock.writeLock();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        //вешаем блокировку, чтобы не пересекаться с flush
        //пересекаться с другими process можно
        dataBufferLockAdd.lock();
        {
            dataBuffer.add(data);
        }
        dataBufferLockAdd.unlock();
        //из мануала: Beware that, unlike in most collections, this method is NOT a constant-time operation. Because of the asynchronous nature of these sets, determining the current number of elements requires traversing them all to count them. Additionally, it is possible for the size to change during execution of this method, in which case the returned result will be inaccurate. Thus, this method is typically not very useful in concurrent applications.
        //поэтому ведем собственный счетчик размера буфера
        if (increaseCurrentCount() >= bufferSize)
            flush();
    }

    public synchronized void flush() {
        dataBufferLockFlush.lock();
        try {
            if (!dataBuffer.isEmpty()) {
                writer.writeBufferedData(dataBuffer.stream().toList());
                dataBuffer.clear();
                resetCurrentCount();
            }
        }
        catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
        finally {
            dataBufferLockFlush.unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }

    private int increaseCurrentCount() {
        synchronized (dataBuffer) {
            dataBufferCurrentCount++;
        }
        return dataBufferCurrentCount;
    }

    private int resetCurrentCount() {
        synchronized (dataBuffer) {
            dataBufferCurrentCount = 0;
        }
        return dataBufferCurrentCount;
    }
}
