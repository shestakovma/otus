package homework.cash;

import homework.cash.exceptions.PackInsufficientException;

//класс Пачка купюр одного номинала
public class CashUnitPack {
    private int count = 0;
    private CashUnit cashUnit = null;

    public CashUnitPack(CashUnit cashUnit, int count) {
        this.cashUnit = cashUnit;
        this.count = count;
    }

    public void add(int count) {
        this.count += count;
    }

    public void remove(int count) throws PackInsufficientException {
        if (count > this.count)
            throw new PackInsufficientException();
        this.count -= count;
    }

    public CashUnit getUnit() {
        return cashUnit;
    }

    public int getCount() {
        return count;
    }

    public boolean contains(int count) {
        return (this.count >= count);
    }
}
