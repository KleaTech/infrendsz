package hu.kleatech.infrendsz.model;

import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_table")
@RequiredArgsConstructor
@ToString(exclude = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@SuppressWarnings("serial")
public class Order implements Serializable {

    @Id @GeneratedValue @Getter private Long id;
    @NonNull @Getter @ManyToOne @JoinColumn(name = "order_column") private Make order;
    @ManyToOne @JoinColumn @NonNull @Getter private Customer customer;
}
