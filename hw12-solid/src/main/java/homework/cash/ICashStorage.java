package homework.cash;

import homework.cash.exceptions.*;

//интерфейс Хранилище купюр
public interface ICashStorage {
    //внести наличные
    void cashIn(CashUnitSet set) throws StorageCapacityOverflowException, StoragePackCountOverflowException;

    //выдать наличные
    CashUnitSet cashOut(int sum) throws PackInsufficientException, NominalInsufficientException, SumOverflowException, NoNominalException, NoCashUnitException;

    //просмотреть баланс
    int getBalance();
}
