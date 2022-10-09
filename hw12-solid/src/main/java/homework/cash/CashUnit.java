package homework.cash;

//класс Купюра
public class CashUnit {
    //номинал купюры
    private int nominal = 0;

    public CashUnit(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }
}
