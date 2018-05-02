package hu.kleatech.infrendsz;

import hu.kleatech.infrendsz.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InfrendszApplication implements ApplicationRunner {

    @Autowired CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(InfrendszApplication.class, args);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //customerService.findAll().stream().map(c -> c.getOrders().toString()).forEach(System.out::println);
    }
}
