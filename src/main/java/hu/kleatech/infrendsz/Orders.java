package hu.kleatech.infrendsz;

import hu.kleatech.infrendsz.model.Customer;
import hu.kleatech.infrendsz.model.Make;
import hu.kleatech.infrendsz.model.Order;
import hu.kleatech.infrendsz.model.StorageUnit;
import hu.kleatech.infrendsz.service.CustomerService;
import hu.kleatech.infrendsz.service.MakeService;
import hu.kleatech.infrendsz.service.OrderService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Named
@Data
@ViewScoped
@SuppressWarnings("serial")
public class Orders implements Serializable {

    private String orderNormal;
    private String orderQuick;
    private String customerNormal;
    private String customerQuick;
    private List<Row> check;
    private String quantity;
    private Make make;
    private Customer cust;

    @Autowired MakeService makeService;
    @Autowired CustomerService customerService;
    @Autowired OrderService orderService;

    public void quickOrder() {
        System.out.println("Quick order: " + orderQuick + ',' + customerQuick + ',' + quantity);
        Make given = makeService.findByExactName(orderQuick);
        if (given==null) {
            message("Cannot find make with the given name!");
            return;
        }
        HashMap<StorageUnit, Integer> map = new HashMap<>(1);
        map.put(given, Integer.parseInt(quantity));
        System.err.println("breakpoint " + map);
        make = makeService.save(new Make(map, "order_" + quantity + "_" + orderQuick));
        cust = customerService.findByExactName(customerQuick);
        if (cust==null) {
            message("Cannot find customer with this name!");
            return;
        }
        doCheck();
    }

    public void normalOrder() {
        System.out.println("Normal order: " + orderNormal + ',' + customerNormal);
        make = makeService.findByExactName(orderNormal);
        if (make==null) {
            message("Cannot find make with the given name!");
            return;
        }
        cust = customerService.findByExactName(customerNormal);
        if (cust==null) {
            message("Cannot find customer with this name!");
            return;
        }
        doCheck();
    }

    private void doCheck() {
        check = new LinkedList<>();
        makeService.availabilityChecker(make).check().forEach(e -> {
            check.add(new Row(e.unit.toMinimalString(), e.have, e.need));
        });
    }

    public List<String> customerComplete(String query) {
        return customerService.find(query).stream().map(c -> c.getName()).collect(Collectors.toList());
    }

    public List<String> orderComplete(String query) {
        return makeService.find(query).stream().map(m -> m.getName()).collect(Collectors.toList());
    }

    public boolean disabled() {
        if (make==null) return true;
        return !makeService.availabilityChecker(make).checkSimple();
    }

    public void send() {
        if (!makeService.availabilityChecker(make).checkSimple()) return;
        orderService.make(new Order(make, cust));
        message("Success");
    }

    private void message(String s) {
        System.out.println(s);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s,  null));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Row {
        private String component;
        private int have;
        private int need;
    }
}