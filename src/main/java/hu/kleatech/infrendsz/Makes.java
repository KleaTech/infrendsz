package hu.kleatech.infrendsz;

import hu.kleatech.infrendsz.model.Component;
import hu.kleatech.infrendsz.model.Make;
import hu.kleatech.infrendsz.model.StorageUnit;
import hu.kleatech.infrendsz.service.ComponentService;
import hu.kleatech.infrendsz.service.MakeService;
import java.io.Serializable;
import java.util.*;
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
public class Makes implements Serializable {

    private List<Row> rows;
    private Row selected;
    private Row adding;

    @Autowired MakeService makeService;
    @Autowired ComponentService componentService;

    @PostConstruct
    public void init() {
        adding = new Row();
        adding.inner = new LinkedList<>();
        rows = new LinkedList<>();
        class Initializer {
            List<Row.Inner> list;

            void init() {
                makeService.findAll().forEach(make -> {
                    list = new LinkedList<>();
                    make.getComponents().entrySet().forEach(entry ->
                        list.add(new Row.Inner(
                            entry.getKey() instanceof Component ? ((Component) entry.getKey()).getSerial().toString() : "",
                            entry.getKey().getName(),
                            entry.getValue().toString())));
                    rows.add(new Row(
                        make.getName(),
                        String.join(", ", make.getComponents().keySet().stream().map(o -> o.toMinimalString()).collect(Collectors.toList())),
                        list,
                        null));
                });
            }
        }
        new Initializer().init();
    }

    public void delete() {
        makeService.delete(selected.getName());
        rows.remove(selected);
        message("Success");
    }

    public void add() {
        Component component = null;
        Make make = null;
        if (adding.components.contains(":")) {
            String serial = adding.components.substring(0, adding.components.indexOf(':')).trim();
            component = componentService.find(Long.parseLong(serial));
        }
        else {
            make = makeService.findByExactName(adding.components);
        }
        if (component==null && make==null) {
            System.out.println("Not a valid component!");
            message("Not a valid ncomponent!");
            return;
        }
        if (component==null) {
            adding.inner.add(new Row.Inner("", adding.components, adding.amount));
            System.out.println("Added " + adding.inner);
            return;
        }
        adding.inner.add(new Row.Inner(component.getSerial().toString(), component.getName(), adding.amount));
        System.out.println("Added " + adding.inner);
    }

    public void save() {
        HashMap<StorageUnit, Integer> map = new HashMap<>(adding.inner.size());
        adding.inner.forEach(i -> {
            StorageUnit unit;
            if (i.serial.isEmpty()) unit = makeService.findByExactName(i.name);
            else unit = componentService.find(Long.parseLong(i.serial));
            map.put(unit, Integer.parseInt(i.amount));
        });
        makeService.save(new Make(map, adding.name));
        close();
        init();
    }

    public void close() {
        adding = new Row();
        adding.inner = new LinkedList<>();
    }

    public List<String> completeMethod(String query) {
        List<String> results = new LinkedList<>();
        makeService.find(query).forEach(e -> results.add(e.toMinimalString()));
        componentService.find(query).forEach(e -> results.add(e.toMinimalString()));
        componentService.findBySerialContaining(query).forEach(e -> results.add(e.toMinimalString()));
        return results.stream().distinct().collect(Collectors.toList());
    }

    private void message(String s) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s, null));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Row {
        private String name;
        private String components;
        private List<Inner> inner;
        private String amount;

        @Data
        @AllArgsConstructor
        public static class Inner {
            private String serial;
            private String name;
            private String amount;
        }
    }
}
