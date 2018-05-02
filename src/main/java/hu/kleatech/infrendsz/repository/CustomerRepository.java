package hu.kleatech.infrendsz.repository;

import hu.kleatech.infrendsz.model.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByNameIgnoreCaseContaining(String name);
    Customer findByNameIgnoreCase(String name);
}
