package homework;


import java.util.*;

public class CustomerReverseOrder {
    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    Deque<Customer> _queue = new LinkedList<Customer>();
    public void add(Customer customer) {
        _queue.add(customer);
    }

    public Customer take() {
        return (Customer)_queue.pollLast();
    }
}
