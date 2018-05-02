package hu.kleatech.infrendsz.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@SuppressWarnings("serial")
public class Make implements Serializable, StorageUnit {

    @Id @GeneratedValue @Getter private Long id;
    @NonNull @Getter @Lob private HashMap<StorageUnit, Integer> receipe;
    @NonNull @Getter @Column(unique=true) private String name;

    @Override public Map<StorageUnit, Integer> getComponents() {
        return Collections.unmodifiableMap(receipe);
    }

    @Override public String toString() {
        return name + ": " + receipe.entrySet().stream().map(e -> e.getValue() + "pcs " + e.getKey().getName()).collect(Collectors.joining(", "));
    }

    @Override public String toMinimalString() {
        return name;
    }
}
