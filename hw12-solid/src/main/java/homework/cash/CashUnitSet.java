package homework.cash;

import homework.cash.exceptions.NoCashUnitException;
import homework.cash.exceptions.PackInsufficientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//класс Пачка купюр разных номиналов
public class CashUnitSet {
    private List<CashUnitPack> storage = new ArrayList<>();

    public void add(CashUnitPack pack) {
        Optional<CashUnitPack> currentPack = storage.stream().filter(m -> m.getUnit().equals(pack.getUnit())).findFirst();
        if (!currentPack.isEmpty()) {
            currentPack.get().add(pack.getCount());
        }
        else {
            storage.add(pack);
        }
    }

    public void add(CashUnitSet set) {
        for (var pack : set.toList()) {
            add(pack);
        }
    }

    public void remove(CashUnitPack pack) throws NoCashUnitException, PackInsufficientException {
        Optional<CashUnitPack> currentPack = storage.stream().filter(m -> m.getUnit().equals(pack.getUnit())).findFirst();
        if (currentPack.isEmpty())
            throw new NoCashUnitException();
        currentPack.get().remove(pack.getCount());
    }

    public void remove(CashUnitSet set) throws NoCashUnitException, PackInsufficientException {
        for (var pack : set.toList()) {
            remove(pack);
        }
    }

    public List<CashUnitPack> toList() {
        return storage;
    }

    public boolean contains(CashUnitPack pack) {
        Optional<CashUnitPack> currentPack = storage.stream().filter(m -> m.getUnit().equals(pack.getUnit())).findFirst();
        return (!currentPack.isEmpty() && currentPack.get().contains(pack.getCount()));
    }

    public boolean contains(CashUnitSet set) {
        boolean result = true;
        for (var pack : set.toList()) {
            result = storage.contains(pack);
            if (!result)
                break;
        }
        return result;
    }
}
