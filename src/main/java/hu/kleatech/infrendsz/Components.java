package hu.kleatech.infrendsz;

import hu.kleatech.infrendsz.model.Component;
import hu.kleatech.infrendsz.service.ComponentService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
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
public class Components implements Serializable {

    private List<Row> rows;
    private Row selected;
    private Row adding;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired ComponentService service;

    @PostConstruct
    public void init() {
        adding = new Row();
        rows = new LinkedList<>();
        service.findAll().forEach(comp -> {
            rows.add(new Row(
                comp.getSerial().toString(),
                comp.getName(),
                comp.getQuantity().toString(),
                comp.getFirstStorage().format(FORMATTER),
                "1"));
        });
    }

    public void delete() {
        service.deleteBySerial(Long.parseLong(selected.serial));
        rows.remove(selected);
        message("Success");
    }

    public void add() {
        try {
            if (service.find(Long.parseLong(adding.serial))!=null) {
                message("Serial must be unique!");
                return;
            }
        }
        catch (NumberFormatException e) {
            message("Serial must be a number!");
            return;
        }
        service.save(new Component(Long.parseLong(adding.serial), LocalDateTime.now(), Integer.parseInt(adding.quantity), adding.name));
        rows.add(new Row(adding.serial, adding.name, adding.quantity, LocalDateTime.now().format(FORMATTER), "1"));
        message("Success");
    }

    public void increase() {
        System.out.println("Quantity=" + selected.addingQuantity);
        Component component = service.find(Long.parseLong(selected.serial));
        int q = selected.addingQuantity.isEmpty() ? 1 : Integer.parseInt(selected.addingQuantity);
        System.out.println("Increasing with " + q);
        component.setQuantity(component.getQuantity() + q);
        selected.setQuantity(component.getQuantity().toString());
        service.save(component);
    }

    public void decrease() {
        Component component = service.find(Long.parseLong(selected.serial));
        int q = selected.addingQuantity.isEmpty() ? 1 : Integer.parseInt(selected.addingQuantity);
        component.setQuantity(component.getQuantity() - q);
        if (component.getQuantity() < 0) component.setQuantity(0);
        selected.setQuantity(component.getQuantity().toString());
        service.save(component);
    }

    private void message(String s) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s,  null));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Row {
        private String serial;
        private String name;
        private String quantity;
        private String firstStorage;
        private String addingQuantity;
    }
}