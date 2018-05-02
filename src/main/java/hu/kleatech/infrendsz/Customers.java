package hu.kleatech.infrendsz;

import hu.kleatech.infrendsz.model.Customer;
import hu.kleatech.infrendsz.service.CustomerService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
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
public class Customers implements Serializable {

    private List<Row> rows;
    private Row selected;
    private Row adding;

    @Autowired CustomerService service;

    @PostConstruct
    public void init() {
        adding = new Row();
        rows = new LinkedList<>();
        service.findAll().forEach(c -> {
            String s = c.getOrders().stream().map(o -> o.getOrder().toMinimalString()).collect(Collectors.joining("; "));
            rows.add(new Row(
                c.getName(),
                s.substring(0, Math.min(200, s.length()))
            ));
        });
    }

    public void add() {
        service.save(new Customer(adding.name));
        rows.add(new Row(adding.name, ""));
        message("Success");
    }

    private void message(String s) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s,  null));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Row {
        private String name;
        private String orders;
        public List<String> orderList() {
            return service.findByExactName(name).getOrders().stream().map(o -> o.getOrder().toString()).collect(Collectors.toList());
        }
    }
}