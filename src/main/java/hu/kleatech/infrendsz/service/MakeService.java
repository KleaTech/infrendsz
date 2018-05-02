package hu.kleatech.infrendsz.service;

import hu.kleatech.infrendsz.model.Component;
import hu.kleatech.infrendsz.model.Make;
import hu.kleatech.infrendsz.model.StorageUnit;
import hu.kleatech.infrendsz.repository.MakeRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MakeService {

    @Autowired MakeRepository makeRepository;
    @Autowired ComponentService componentService;

    private AvailabilityChecker cached = null;

    public Make save(Make make) {
        return makeRepository.save(make);
    }
    public List<Make> find(String name) {
        return makeRepository.findByNameIgnoreCaseContaining(name);
    }
    public Make findByExactName(String name) {
        return makeRepository.findByNameIgnoreCase(name);
    }
    public List<Make> findAll() {
        return makeRepository.findAll();
    }
    public void delete(Long id) {
        makeRepository.deleteById(id);
    }
    public void delete(String name) {
        makeRepository.delete(makeRepository.findByNameIgnoreCase(name));
    }
    public AvailabilityChecker availabilityChecker(Make make) {
        return new AvailabilityChecker(make);
    }

    public class AvailabilityChecker {

        private final Make make;
        private boolean operating = true;
        private Map<StorageUnit, Integer> temp;
        private final Map<StorageUnit, Integer> linearMap = new HashMap<>(10);
        private AvailabilityChecker(Make make) {
            if (make==null) throw new NullPointerException("Null Make in AvailabilityChecker");
            this.make = make;
        }

        private void linearizeMake() {
            linearMap.put(make, 1);
            while (true) {
                operating = false;
                temp = new HashMap<>(10);
                linearMap.entrySet().forEach(unit -> {
                    if (unit.getKey() instanceof Component) putAdd(temp, unit.getKey(), unit.getValue());
                    else {
                        Stream.iterate(0, i -> i+1).limit(unit.getValue()).forEach(action ->
                            putAllAdd(temp, unit.getKey().getComponents()));
                        operating = true;
                    }
                });
                if (!operating) break;
                linearMap.clear();
                linearMap.putAll(temp);
                System.out.println("Linearizing map: " + linearMap);
            }
        }

        private <K> void putAdd(Map<K, Integer> map, K key, Integer value) {
            map.put(key, map.getOrDefault(key, 0) + value);
        }

        private <K> void putAllAdd(Map<K, Integer> map, Map<K, Integer> newMap) {
            newMap.entrySet().forEach(entry -> putAdd(map, entry.getKey(), entry.getValue()));
        }

        public List<Entry> check() {
            if (operating) linearizeMake();
            List<Entry> ret = new ArrayList<>(linearMap.size());
            linearMap.entrySet().forEach(entry -> {
                Component temporal = (Component) entry.getKey();
                Component actual = componentService.find(temporal.getSerial());
                if (actual==null) actual=new Component(temporal.getSerial(), temporal.getFirstStorage(), 0, entry.getKey().getName() + "[DELETED]");
                ret.add(new Entry(actual, entry.getValue(), actual.getQuantity()));
            });
            return ret;
        }

        public boolean checkSimple() {
            if (operating) linearizeMake();
            return check().stream().noneMatch(entry -> (entry.have<entry.need));
        }

        @Data
        public class Entry {
            public final Component unit;
            public final Integer need;
            public final Integer have;
        }
    }
}
