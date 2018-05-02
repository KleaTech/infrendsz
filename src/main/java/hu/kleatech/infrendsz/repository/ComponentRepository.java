package hu.kleatech.infrendsz.repository;

import hu.kleatech.infrendsz.model.Component;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long>{
    Component findBySerial(Long serial);
    List<Component> findByNameIgnoreCaseContaining(String name);
    @Query(value = "select * from component where serial like %?1%", nativeQuery = true)
    List<Component> findBySerialContaining(String serial);
}
