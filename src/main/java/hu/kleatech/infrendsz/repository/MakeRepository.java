package hu.kleatech.infrendsz.repository;

import hu.kleatech.infrendsz.model.Make;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakeRepository extends JpaRepository<Make, Long> {
    List<Make> findByNameIgnoreCaseContaining(String name);
    Make findByNameIgnoreCase(String name);
}