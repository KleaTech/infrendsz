package hu.kleatech.infrendsz.service;

import hu.kleatech.infrendsz.model.Component;
import hu.kleatech.infrendsz.model.Customer;
import hu.kleatech.infrendsz.model.Order;
import hu.kleatech.infrendsz.repository.OrderRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private @Autowired OrderRepository orderRepository;
    private @Autowired MakeService makeService;
    private @Autowired ComponentService componentService;

    public Order make(Order order) throws IllegalArgumentException {
        MakeService.AvailabilityChecker checker = makeService.availabilityChecker(order.getOrder());
        orderRepository.save(order);
        if (!checker.checkSimple()) throw new IllegalArgumentException("Not enough components are available.");
        checker.check().forEach(entry -> {
            Component comp = entry.unit;
            comp.setQuantity(comp.getQuantity()-entry.need);
            componentService.save(comp);
        });
        return null;
        //return orderRepository.save(order);
    }
    public List<Order> find(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @SuppressWarnings("serial")
    public static class NotAvailableException extends RuntimeException {
        public NotAvailableException() {super();}
        public NotAvailableException(String message) {super(message);}
        public NotAvailableException(String message, Throwable cause) {super(message, cause);}
        public NotAvailableException(Throwable cause) {super(cause);}
    }
}