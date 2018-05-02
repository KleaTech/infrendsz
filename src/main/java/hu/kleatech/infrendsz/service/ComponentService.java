package hu.kleatech.infrendsz.service;

import hu.kleatech.infrendsz.model.Component;
import hu.kleatech.infrendsz.repository.ComponentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentService {

    private @Autowired ComponentRepository componentRepository;

    public Component save(Component component) {
        return componentRepository.save(component);
    }
    public List<Component> find(String name) {
        return componentRepository.findByNameIgnoreCaseContaining(name);
    }
    public Component find(Long serial) {
        return componentRepository.findBySerial(serial);
    }
    public List<Component> findBySerialContaining(String serial) {
        return componentRepository.findBySerialContaining(serial);
    }
    public List<Component> findAll() {
        return componentRepository.findAll();
    }
    public void deleteBySerial(Long serial) {
        componentRepository.delete(componentRepository.findBySerial(serial));
    }
    public void delete(Long id) {
        componentRepository.deleteById(id);
    }
}
