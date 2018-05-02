package hu.kleatech.infrendsz.repository;

import hu.kleatech.infrendsz.model.Customer;
import hu.kleatech.infrendsz.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}