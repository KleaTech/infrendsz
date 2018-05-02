package hu.kleatech.infrendsz.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import javax.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = {"id", "firstStorage"})
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@SuppressWarnings("serial")
public class Component implements Serializable, StorageUnit {
    @Id @GeneratedValue @Getter private Long id;
    @Getter @NonNull private Long serial;
    @Getter @NonNull private LocalDateTime firstStorage;
    @Getter @Setter @NonNull private Integer quantity;
    @Getter @NonNull private String name;

    @Override public Map<StorageUnit, Integer> getComponents() {
        return null;
    }

    @Override public String toMinimalString() {
        return serial + ":" + name;
    }
}
