package homework.ATM;

import homework.cash.*;
import homework.cash.exceptions.*;

import java.util.Comparator;
import java.util.Optional;

//класс Банкомат
public class ATM implements ICashStorage {
    //макс. кол-во купюр в ячейке
    private final int MAX_PACK_CAPACITY = 10000;
    //макс. кол-во ячеек
    private final int MAX_PACK_COUNT = 10;

    private CashUnitSet storage = new CashUnitSet();

    //внести наличные
    @Override
    public void cashIn(CashUnitSet set) throws StorageCapacityOverflowException, StoragePackCountOverflowException {
        //проверим, можно ли запихнуть в банкомат
        for (var pack : set.toList()) {
            Optional<CashUnitPack> currentPack = storage.toList().stream().filter(m -> m.getUnit().equals(pack.getUnit())).findFirst();
            //нет места для нового номинала
            if (currentPack.isEmpty() && storage.toList().size() == MAX_PACK_COUNT) {
                throw new StoragePackCountOverflowException();
            }
            //ячейка под номинал переполнена
            if ((!currentPack.isEmpty() ? currentPack.get().getCount() : 0) + pack.getCount() > MAX_PACK_CAPACITY) {
                throw new StorageCapacityOverflowException();
            }
            storage.add(pack);
        }
    }

    //выдать наличные
    @Override
    public CashUnitSet cashOut(int sum) throws SumOverflowException, NoNominalException, PackInsufficientException, NominalInsufficientException, NoCashUnitException {
        CashUnitSet result = new CashUnitSet();
        //проверяем общий баланс
        if (getBalance() < sum) {
            throw new SumOverflowException();
        }
        //проверяем минимальное наличие номиналов
        Optional<CashUnitPack> minimalPackExisting = storage.toList()
                .stream().filter(m -> sum % m.getUnit().getNominal() == 0)
                .sorted(Comparator.comparingInt(m -> m.getUnit().getNominal()))
                .findFirst();
        if (minimalPackExisting.isEmpty()) {
            throw new NoNominalException();
        }
        //худший сценарий - все купюры минимального номинала
        var minimalPack = new CashUnitPack(minimalPackExisting.get().getUnit(), sum / minimalPackExisting.get().getUnit().getNominal());
        //пробуем заменять на более крупный номинал, начиная с самого крупного
        for(CashUnitPack pack : storage.toList()
                .stream().filter(m -> m.getUnit().getNominal() % minimalPack.getUnit().getNominal() == 0)
                .sorted(Comparator.comparingInt(m -> ((CashUnitPack)m).getUnit().getNominal()).reversed())
                .toList()) {
            int count = pack.getCount();
            while (count > 0 && (minimalPack.getCount() * minimalPack.getUnit().getNominal()) / pack.getUnit().getNominal() > 0) {
                result.add(new CashUnitPack(pack.getUnit(), 1));
                count--;
                minimalPack.remove(pack.getUnit().getNominal() / minimalPack.getUnit().getNominal());
            }
        }
        //все, что могли, заменили на более крупные номиналы
        if (minimalPack.getCount() > 0) {
            result.add(minimalPack);
            //проверяем наименьший по кол-ву
            if (!storage.contains(new CashUnitPack(minimalPack.getUnit(), minimalPack.getCount())))
                throw new NominalInsufficientException();
        }
        //забираем пачку
        storage.remove(result);
        return result;
    }

    //просмотреть баланс
    @Override
    public int getBalance() {
        int result = 0;
        for (var pack : storage.toList()) {
            result += pack.getCount() * pack.getUnit().getNominal();
        }
        return result;
    }
}
