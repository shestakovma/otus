package homework;

import homework.ATM.ATM;
import homework.cash.CashUnit;
import homework.cash.CashUnitPack;
import homework.cash.CashUnitSet;
import homework.cash.exceptions.NoNominalException;
import homework.cash.exceptions.StorageCapacityOverflowException;
import homework.cash.exceptions.StoragePackCountOverflowException;
import homework.cash.exceptions.SumOverflowException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ATMTest {
    @Test
    @DisplayName("Sum correctness")
    void sumIsCorrect() {
        var cashSet = new CashUnitSet();
        cashSet.add(new CashUnitPack(new CashUnit(50), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(100), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(500), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(1000), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5000), 10000));

        boolean added = true;

        var atm = new ATM();
        try {
            atm.cashIn(cashSet);
        }
        catch (Exception ex) {
            added = false;
        }
        assertThat(added).isEqualTo(true);

        int balanceInit = atm.getBalance();
        int sumTotal = 0;
        for (int i = 0; i < 10; i++) {
            int sum = 50 * ThreadLocalRandom.current().nextInt(1, 2550);
            sumTotal += sum;
            boolean sumGot = true;
            try {
                CashUnitSet gotSet = atm.cashOut(sum);
                int realSum = 0;
                for (var pack : gotSet.toList()) {
                    realSum += pack.getCount() * pack.getUnit().getNominal();
                }
                assertThat(realSum).isEqualTo(sum);
            }
            catch (Exception ex) {
                sumGot = false;
            }
            assertThat(sumGot).isEqualTo(true);
        }

        int balanceFinal = atm.getBalance();
        assertThat(sumTotal).isEqualTo(balanceInit - balanceFinal);
    }

    @Test
    @DisplayName("NominalInsufficientException")
    void nominalInsufficientException() {
        var cashSet = new CashUnitSet();
        cashSet.add(new CashUnitPack(new CashUnit(50), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(100), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(500), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(1000), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5000), 10000));

        boolean added = true;

        var atm = new ATM();
        try {
            atm.cashIn(cashSet);
        }
        catch (Exception ex) {
            added = false;
        }
        assertThat(added).isEqualTo(true);

        int balanceInit = atm.getBalance();
        int sumTotal = 0;
        for (int i = 0; i < 10; i++) {
            int sum = 50 * ThreadLocalRandom.current().nextInt(1, 2550);
            sumTotal += sum;
            boolean sumGot = true;
            try {
                CashUnitSet gotSet = atm.cashOut(sum);
                int realSum = 0;
                for (var pack : gotSet.toList()) {
                    realSum += pack.getCount() * pack.getUnit().getNominal();
                }
                assertThat(realSum).isEqualTo(sum);
            }
            catch (Exception ex) {
                sumGot = false;
            }
            assertThat(sumGot).isEqualTo(true);
        }

        int balanceFinal = atm.getBalance();
        assertThat(sumTotal).isEqualTo(balanceInit - balanceFinal);
    }

    @Test
    @DisplayName("NoNominalException")
    void noNominalException() {
        var cashSet = new CashUnitSet();
        cashSet.add(new CashUnitPack(new CashUnit(5000), 10000));

        boolean added = true;

        var atm = new ATM();
        try {
            atm.cashIn(cashSet);
        }
        catch (Exception ex) {
            added = false;
        }
        assertThat(added).isEqualTo(true);

        boolean exceptionFired = false;

        try {
            int sum = 7500;
            atm.cashOut(sum);
        }
        catch (NoNominalException ex) {
            exceptionFired = true;
        }
        catch (Exception ex) {

        }
        assertThat(exceptionFired).isEqualTo(true);
    }

    @Test
    @DisplayName("StorageCapacityOverflowException")
    void storageCapacityOverflowException() {
        var cashSet = new CashUnitSet();
        cashSet.add(new CashUnitPack(new CashUnit(5000), 100000));

        boolean exceptionFired = false;
        var atm = new ATM();
        try {
            atm.cashIn(cashSet);
        }
        catch (StorageCapacityOverflowException ex) {
            exceptionFired = true;
        }
        catch (Exception ex) {
        }
        assertThat(exceptionFired).isEqualTo(true);
    }

    @Test
    @DisplayName("StoragePackCountOverflowException")
    void storagePackCountOverflowException() {
        var cashSet = new CashUnitSet();
        cashSet.add(new CashUnitPack(new CashUnit(5000), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5001), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5002), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5003), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5004), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5005), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5006), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5007), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5008), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5009), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5010), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5011), 10000));
        cashSet.add(new CashUnitPack(new CashUnit(5012), 10000));

        boolean exceptionFired = false;
        var atm = new ATM();
        try {
            atm.cashIn(cashSet);
        }
        catch (StoragePackCountOverflowException ex) {
            exceptionFired = true;
        }
        catch (Exception ex) {
        }
        assertThat(exceptionFired).isEqualTo(true);
    }

    @Test
    @DisplayName("SumOverflowException")
    void sumOverflowException() {
        var cashSet = new CashUnitSet();
        cashSet.add(new CashUnitPack(new CashUnit(500), 5));

        boolean added = true;

        var atm = new ATM();
        try {
            atm.cashIn(cashSet);
        }
        catch (Exception ex) {
            added = false;
        }
        assertThat(added).isEqualTo(true);

        boolean exceptionFired = false;

        try {
            int sum = 5000;
            atm.cashOut(sum);
        }
        catch (SumOverflowException ex) {
            exceptionFired = true;
        }
        catch (Exception ex) {

        }
        assertThat(exceptionFired).isEqualTo(true);
    }
}