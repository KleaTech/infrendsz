package hu.kleatech.infrendsz.service;

import hu.kleatech.infrendsz.model.Customer;
import hu.kleatech.infrendsz.repository.CustomerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private @Autowired CustomerRepository customerRepository;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
    public List<Customer> find(String name) {
        return customerRepository.findByNameIgnoreCaseContaining(name);
    }
    public Customer findByExactName(String name) {
        return customerRepository.findByNameIgnoreCase(name);
    }
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
