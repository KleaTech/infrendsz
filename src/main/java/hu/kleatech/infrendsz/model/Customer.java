package hu.kleatech.infrendsz.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = "orders")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@SuppressWarnings("serial")
public class Customer implements Serializable {
    @Id @GeneratedValue @Getter private Long id;
    @NonNull @Getter @Column(unique = true) private String name;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER) @Getter @Setter private Set<Order> orders;
}
