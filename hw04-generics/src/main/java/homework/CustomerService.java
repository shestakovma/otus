package homework;


import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private TreeMap<Customer, String> _map = new TreeMap<>(
                    (k1, k2) ->
                    {
                        return k1.getScores() < k2.getScores() ? -1 : (k1.getScores() == k2.getScores() ? 0 : 1);
                    });

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk

        //возвращаем копии объектов, чтобы изменения объектов извне не влияли на орбъекты в коллекции
        var res = _map.firstEntry();
        if (res == null)
            return null;
        return new AbstractMap.SimpleEntry<>(new Customer(res.getKey()), res.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        //возвращаем копии объектов, чтобы изменения объектов извне не влияли на орбъекты в коллекции
        var res = _map.higherEntry(customer);
        if (res == null)
            return null;
        return new AbstractMap.SimpleEntry<>(new Customer(res.getKey()), res.getValue());
    }

    public void add(Customer customer, String data) {
        _map.put(customer, data);
    }
}
